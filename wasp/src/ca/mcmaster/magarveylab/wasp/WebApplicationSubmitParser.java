package ca.mcmaster.magarveylab.wasp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import ca.mcmaster.magarveylab.wasp.session.FileUploadListener;
import ca.mcmaster.magarveylab.wasp.session.Session;
import ca.mcmaster.magarveylab.wasp.session.SessionListener;
import ca.mcmaster.magarveylab.wasp.session.SessionManager;

/**
 * Parse an HTTP request into Java objects to instantiate a new instance of a web application. 
 * @author skinnider
 *
 */
public abstract class WebApplicationSubmitParser {

	protected static final Logger logger = Logger.getLogger(WebApplicationSubmitParser.class.getName());

	/**
	 * Parse a HttpServletRequest to create a new session and set its upload handler. 
	 * @param request	the request to parse
	 * @param uploadHandler	the upload handler to associate with the session
	 * @return	the new session
	 * @throws IOException
	 */
	public static Session parseSession(HttpServletRequest request, ServletFileUpload uploadHandler) throws IOException {
		SessionManager sessionManager = SessionManager.getSessionManager();
		Session session = null;

		ServletInputStream in = request.getInputStream();
		byte b[] = new byte[256];
		for (int i = 0; i < 4; i++) {
			Arrays.fill(b, (byte) 0);
			in.readLine(b, 0, 256);
		}
		String sessionID = new String(b, 0, 256);
		sessionID = sessionID.trim();
		logger.log(Level.INFO, "Session: " + sessionID);
		session = sessionManager.getSession(sessionID);

		// set listener
		SessionListener listener = new SessionListener();
		session.setListener(listener);
		
		// set upload progress listener
		FileUploadListener uploadListener = new FileUploadListener();
		uploadHandler.setProgressListener(uploadListener);
		session.listener().uploadListener = uploadListener;
		
		// set context name
		String context = request.getContextPath().replace("/", "");
		session.setContext(context);
		
		// make sure session directory exists
		File sessionDir = new File(session.dir());
		if (!sessionDir.isDirectory()) 
			sessionDir.mkdir();
		
		return session;
	}
	
	/**
	 * Parse a FileItem object into local file system, writing the file to its final destination.
	 * @param items		FileItem objects to parse
	 * @param session	current session
	 * @return			local filepath of the file
	 * @throws Exception	if an error occurs writing a file
	 */
	public static String parseFileItem(FileItem item, Session session) throws Exception {
		File localFile = new File(item.getName());
		String webFileName = localFile.getName();

		// Write file to the ultimate location.
		File file = new File(session.dir(), webFileName);
		if (!file.exists()) {
			item.write(file);
			logger.log(Level.INFO, "Wrote file " + file.getAbsolutePath());
		}

		return session.dir() + File.separator + webFileName;
	}
	
}
