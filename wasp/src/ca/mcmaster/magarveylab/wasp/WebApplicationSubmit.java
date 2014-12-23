package ca.mcmaster.magarveylab.wasp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.mcmaster.magarveylab.wasp.report.Report;
import ca.mcmaster.magarveylab.wasp.session.Session;
import ca.mcmaster.magarveylab.wasp.session.SessionManager;
import ca.mcmaster.magarveylab.wasp.util.TimeUtil;

/**
 * Servlet for a web application search.
 * @author skinnider
 *
 */
public class WebApplicationSubmit extends HttpServlet {
	
	private static final long serialVersionUID = 1723246115918154801L;
	protected static SessionManager sessionManager;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sessionManager = SessionManager.getSessionManager();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionID = request.getParameter("sessionID");
		Session session = sessionManager.getSession(sessionID);
		
		// Return nothing if there is no active session
		if (session == null)
			return;

		setHeartBeat(session);
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer();
		
		Report report = session.report();
		String html = report.html();
		sb.append(html);
		
		out.println(sb.toString());
		out.flush();
		out.close();
	}
		
	protected void setHeartBeat(Session session) {
		String heartbeat = TimeUtil.getTimeTag();
		session.setLastHeartBeat(heartbeat);
	}
	
}
