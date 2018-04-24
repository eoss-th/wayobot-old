package com.eoss.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

@SuppressWarnings("serial")
public class StorageServlet extends HttpServlet {

	public static final boolean SERVE_USING_BLOBSTORE_API = false;
	
	private static final String bucket = System.getenv("bucket");

	/**
	 * This is where backoff parameters are configured. Here it is aggressively retrying with
	 * backoff, up to 10 times but taking no more that 15 seconds total to do so.
	 */
	private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
			.initialRetryDelayMillis(10)
			.retryMaxAttempts(10)
			.totalRetryPeriodMillis(15000)
			.build());

	/**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		/*String [] paths = req.getRequestURI().split("/", 3);
		System.out.println(req.getRequestURI() +"::"+paths[2]);*/
		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename(req), 0, BUFFER_SIZE);
		copy(Channels.newInputStream(readChannel), resp.getOutputStream());		
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel outputChannel;
		outputChannel = gcsService.createOrReplace(gcsFilename(req), instance);
		
		copy(req.getInputStream(), Channels.newOutputStream(outputChannel));
	}
	
	private GcsFilename gcsFilename(HttpServletRequest req) {
		// s/a/b
		String [] paths = req.getRequestURI().split("/", 3);
		System.out.println(req.getRequestURI() +"::"+paths[2]);
		return	new GcsFilename(bucket, paths[2]);
	}

	/**
	 * Transfer the data from the inputStream to the outputStream. Then close both streams.
	 */
	private void copy(InputStream input, OutputStream output) throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			
			int bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			input.close();
			output.close();
		}
	}
}