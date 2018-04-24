package com.eoss.servlet;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.eoss.appengine.bean.ShowCase;
import com.eoss.appengine.dao.ShowCaseDAO;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

@SuppressWarnings("serial")
public class BinaryStorageServlet extends HttpServlet {
	
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
	private static final int BUFFER_SIZE = 5 * 1024 * 1024;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
		GcsFilename fileName = gcsFilename(req);	
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	    BlobKey blobKey = blobstoreService.createGsBlobKey(
	        "/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
	    blobstoreService.serve(blobKey, resp);		
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel outputChannel;
		
		outputChannel = gcsService.createOrReplace(gcsFilename(req), instance);
		ServletFileUpload upload = new ServletFileUpload();
		try {
			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				copy(iterator.next().openStream(), Channels.newOutputStream(outputChannel));				
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String [] paths = req.getRequestURI().split("/");
		String botId = paths[3];
		ShowCase showcase = new ShowCase();
		ShowCaseDAO showcaseDao = new ShowCaseDAO();
		Entity ent = showcaseDao.getShowCase(botId);
		if(ent == null) {
			showcase.setBotId(botId);
			showcase.setHasImage(true);
			showcase.setPublish(false);
			showcase.setViewCount(0);
			showcaseDao.addShowCase(showcase);	
		}
	}
	
	private GcsFilename gcsFilename(HttpServletRequest req) {
		String [] paths = req.getRequestURI().split("/", 3);
		
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
	
	@SuppressWarnings("unused")
	private void copy(byte[]buffer, OutputStream output) throws IOException {
		try {
			output.write(buffer, 0, buffer.length);
		} finally {
			output.flush();
			output.close();
		}
	}
	
}