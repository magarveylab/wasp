package ca.mcmaster.magarveylab.wasp.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import ca.mcmaster.magarveylab.wasp.session.SessionListener;

/**
 * Handles exceptions thrown by a web application. 
 * @author skinnider
 *
 */
public class ExceptionHandler {
	
	private SessionListener listener;
	private final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());
	
	/**
	 * Instantiate a new exception handler for this session.
	 * @param listener	session listener for this web application
	 */
	public ExceptionHandler(SessionListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Throw a new exception through this exception handler.
	 * @param e		exception to throw
	 */
	public void throwException(Exception e) {
		logger.log(Level.SEVERE, e.getMessage());
		listener.throwException(e);
	}

}