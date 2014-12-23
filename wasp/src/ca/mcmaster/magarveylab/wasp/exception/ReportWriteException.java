package ca.mcmaster.magarveylab.wasp.exception;

/**
 * Error thrown when a web application cannot write the report. 
 * @author skinnider
 *
 */
public class ReportWriteException extends Exception {

	private static final long serialVersionUID = -5826007116892183624L;

	public ReportWriteException() {
		super();
	}

	public ReportWriteException(String m) {
		super(m);
	}

	public ReportWriteException(String m, Exception e) {
		super(m,e);
	}

}
