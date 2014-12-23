package ca.mcmaster.magarveylab.wasp.report;

import java.io.IOException;

/**
 * A web application report, containing output to the user in textual form and HTML page writing functionality. 
 * @author skinnider
 *
 */
public interface Report {
	
	public void run();
	
	public void write() throws IOException;
	
	public String html();
	
	public void terminate();

	public boolean isTerminated();
	
}
