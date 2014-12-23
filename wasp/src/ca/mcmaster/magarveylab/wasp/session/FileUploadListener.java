package ca.mcmaster.magarveylab.wasp.session;

import org.apache.tomcat.util.http.fileupload.ProgressListener;

/**
 * Get the progress of a file upload. 
 * @author skinnider
 *
 */
public class FileUploadListener implements ProgressListener {

	private volatile long bytesRead = 0L, contentLength = 0L, item = 0L;

	public FileUploadListener() {
		super();
	}

	public void update(long aBytesRead, long aContentLength, int anItem) {
		bytesRead = aBytesRead;
		contentLength = aContentLength;
		item = anItem;
	}

	public long getBytesRead() {
		return bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public long getItem() {
		return item;
	}
	
}