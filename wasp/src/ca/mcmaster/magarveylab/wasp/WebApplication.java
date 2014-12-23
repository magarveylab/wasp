package ca.mcmaster.magarveylab.wasp;

public interface WebApplication {

	public void run() throws Exception;
	
	public void terminate();
	
	public boolean isTerminated();
	
}
