package ca.mcmaster.magarveylab.wasp.session;

import ca.mcmaster.magarveylab.wasp.WebApplication;
import ca.mcmaster.magarveylab.wasp.exception.ExceptionHandler;
import ca.mcmaster.magarveylab.wasp.report.Report;

public interface Session {

	/**
	 * Get the listener object that reports progress and reports to the user.
	 * @return	session listener object
	 */
	public SessionListener listener();
	
	/**
	 * Set the session listener object that reports progress and reports to the user.
	 * @param listener	session listener object
	 */
	public void setListener(SessionListener listener);
	
	/**
	 * Get the session directory, equivalent to [context]/tasks/[session ID]/. 
	 * Trailing slash is included in the returned value.
	 * @return	the session directory
	 */
	public String dir();

	/**
	 * Set the session directory.
	 * @param dir	the session directory
	 */
	public void setDir(String dir);

	/**
	 * Get the absolute path of the context root. Trailing slash in included in the returned value. 
	 * @return	the absolute path of the context root
	 */
	public String root();
	
	/**
	 * Set the absolute path of the context root.
	 * @param root	the absolute path of the context root
	 */
	public void setRoot(String root);

	/**
	 * Get the context of this webapp, i.e. the name of the .war file deployed by Tomcat.
	 * @return	webapp context
	 */
	public String context();

	/**
	 * Set the context of this webapp from a HttpServletRequest. 
	 * @param context	webapp context
	 */
	public void setContext(String context);

	/**
	 * Get the session ID.
	 * @return	the session ID
	 */
	public String id();

	/**
	 * Set the session ID.
	 * @param id	the session ID
	 */
	public void setID(String id);
	
	/**
	 * Get the exception handler that throws exceptions to the front end and command line.
	 * @return	exception handler object
	 */
	public ExceptionHandler exceptionHandler();

	/**
	 * Set the exception handler that throws exceptions to the front end and command line.
	 * @param exceptionHandler	exception handler object
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler);
	
	public Report report();

	public void setReport(Report report);

	public String lastHeartBeat();

	public void setLastHeartBeat(String heartbeat);

	public WebApplication webapp();

	public void setWebapp(WebApplication webapp);

	public String subDir(String dir);
	
	public String webDir();

}
