package com.eoss.brain.net.gcs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.eoss.brain.net.Context;
import com.eoss.brain.net.Node;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

@SuppressWarnings("serial")
public class GcsContext extends Context {

	/**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	
	private final GcsService	gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
			.initialRetryDelayMillis(10)
			.retryMaxAttempts(10)
			.totalRetryPeriodMillis(15000)
			.build());
	
	private final GcsFilename gcsFilename;
	
	public GcsContext(String bucket, String name) {
		super(name);
		gcsFilename = new GcsFilename(bucket, name + SUFFIX);
	}

	@Override
	protected void doLoad(String name) throws Exception {		
		BufferedReader br = null;
        try {
        		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, BUFFER_SIZE);
            br = new BufferedReader(new InputStreamReader(Channels.newInputStream(readChannel), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine())!=null) {
                sb.append(line);
            }
            loadJSON(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br!=null) try { br.close(); } catch (Exception e) {}
        }
	}

	@Override
	protected void doSave(String name, List<Node> nodeList) {
		System.out.println("doSave : "+nodeList.size());
		OutputStreamWriter out = null;
		try {
			GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
			GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, instance);
            out = new OutputStreamWriter(Channels.newOutputStream(outputChannel), StandardCharsets.UTF_8);
            out.write(toJSONString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out!=null) try { out.close(); } catch (Exception e) {}
        }
	}

}
