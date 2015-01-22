package ca.mcmaster.magarveylab.wasp.session;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Reports Clams search progress to user.
 * @author skinnider
 *
 */
public class SessionListener {

    private volatile LinkedHashMap<String,String> stages;
    private volatile int progress; // progress of the search, out of 100
    public FileUploadListener uploadListener;
    public String report;
    
    public SessionListener() {
    	stages = new LinkedHashMap<String,String>();
    }
    
    public synchronized String getStage(int idx) {
    	String key = (new ArrayList<String>(stages.keySet())).get(idx);
    	return key;
    }
    
    public synchronized String getDetail(int idx) {
    	String value = (new ArrayList<String>(stages.values())).get(idx);
    	return value;
    }
    
    public synchronized void addStage(String stage, String detail) {
    	stages.put(stage, detail);
    	System.out.println(detail);
    }
    
    public synchronized void updateLastDetail(String detail) {
    	int idx = stages.size() - 1;
    	String key = (new ArrayList<String>(stages.keySet())).get(idx);
    	stages.put(key, detail);
    	System.out.println(detail);
    }
    
    public synchronized int progress() {
    	return progress;
    }
    
    public synchronized void updateProgress(int i) {
    	progress = i;
    }
    
    public synchronized int size() {
    	return stages.size();
    }
    
    public synchronized void throwException(Exception e) {
    	// Set the exception message
    	String exception = e.toString();
    	// Convert the stack trace to a string
    	StringWriter trace = new StringWriter();
    	e.printStackTrace(new PrintWriter(trace));
    	String details = trace.toString();
    	// Output the exception message & stack trace	
    	addStage(exception, details);
    	System.err.println("EXCEPTION: " + exception + "\n");
    	e.printStackTrace();
    }
    
    public void attachReport(String report) {
    	this.report = report;
    }
    
    public String report() {
    	return report;
    }

}
