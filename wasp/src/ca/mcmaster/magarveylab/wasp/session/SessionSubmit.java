package ca.mcmaster.magarveylab.wasp.session;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.mcmaster.magarveylab.wasp.exception.ExceptionHandler;
import ca.mcmaster.magarveylab.wasp.util.TimeUtil;

/**
 * Servlet to register a web application session. 
 * @author skinnider
 * 
 */
public abstract class SessionSubmit extends HttpServlet {
	
	private static final long serialVersionUID = -5671961856226916741L;
	private static SessionManager sessionManager;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sessionManager = SessionManager.getSessionManager();
		
		// check that the "tasks" directory exists; if not, create it
		String realPath = getServletContext().getRealPath("tasks");
		File destinationDir = new File(realPath);
		if (!destinationDir.isDirectory()) {
			destinationDir.mkdir();
		}
	}

	private void setHeartBeat(Session session) {
		String heartbeat = TimeUtil.getTimeTag();
		session.setLastHeartBeat(heartbeat);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sessionID = request.getParameter("sessionID");
		Session session = sessionManager.getSession(sessionID);
		
		if (session == null) 
			return;
		
		PrintWriter out = response.getWriter();
		StringBuffer buffer = new StringBuffer();

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		out.println(buffer.toString());
		out.flush();
		out.close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		// check if the request is registering a new session 
		String isRegister = request.getParameter("register");
		if (isRegister != null && isRegister.equals("on")) {
			String sessionID = request.getParameter("sessionID");
			// register new session
			registerSession(sessionID, request);
			// response session ID
			responseSessionID(sessionID, response);
		}
	}

	/**
	 * Register new session. Old session with same ID will be overwritten.
	 * @param sessionID 
	 * @param request
	 */
	private void registerSession(String sessionID, HttpServletRequest request) {
		// if an old session with the same id already exists, unregister it
		Session old = sessionManager.getSession(sessionID);
		if (old != null)
			sessionManager.removeSession(sessionID);
		
		// create new session and set ID, root, exception handler & heartbeat
		Session session = new Session();
		session.setID(sessionID);
		SessionListener listener = new SessionListener();
		session.setListener(listener);
		String root = request.getServletContext().getRealPath(File.separator);
		session.setRoot(root);
		String dir = root + "tasks" + File.separator + sessionID + File.separator;
		session.setDir(dir);
		ExceptionHandler exceptionHandler = new ExceptionHandler(listener);
		session.setExceptionHandler(exceptionHandler);
		setHeartBeat(session);
		
		// register the session
		sessionManager.addSession(sessionID, session);
	}

	/**
	 * response with current session ID
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void responseSessionID(String sessionID, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		StringBuffer buffer = new StringBuffer();

		response.setContentType("text/xml");
		buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		buffer.append("<response>\n");
		buffer.append("<sessionID>" + sessionID + "</sessionID>\n");
		buffer.append("</response>\n");

		out.println(buffer.toString());
		out.flush();
		out.close();
	}
	
}