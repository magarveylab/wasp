package ca.mcmaster.magarveylab.wasp.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;

import ca.mcmaster.magarveylab.wasp.exception.ReportWriteException;
import ca.mcmaster.magarveylab.wasp.session.FileUploadListener;
import ca.mcmaster.magarveylab.wasp.session.Session;
import ca.mcmaster.magarveylab.wasp.session.SessionListener;

/**
 * A generic web application report, which can be extended for application-specific functionality.  
 * @author skinnider
 *
 */
public abstract class BasicReport extends TimerTask implements Report {
	
	protected boolean terminated;
	protected String html;
	protected Session session;
	
	public BasicReport(Session session) {
		this.session = session;
	}

	@Override
	public void run() {
		// continuously update the html variable without needing a get request
		try {
			StringBuffer sb = new StringBuffer();

			String uploadProgress = uploadProgress();
			String searchProgress = searchProgress();
			sb.append(uploadProgress);
			sb.append(searchProgress);

			if (terminated) {
				String reportHtml = getReportHtml();
				sb.append(reportHtml);
			}

			html = sb.toString();
			write();
		} catch (IOException e) {
			ReportWriteException exception = new ReportWriteException("Error: could not write report", e);
			session.exceptionHandler().throwException(exception);
		} 
	}

	/**
	 * Report the progress of an ongoing web application search.
	 * @return	the progress as HTML
	 */
	public String searchProgress() {
		StringBuffer sb = new StringBuffer();

		SessionListener listener = session.listener();
		if (listener == null)
			return "";

		for (int i = 0; i < listener.size(); i++) {
			if (listener.getStage(i) != null) {
				sb.append("<div class='stageHolder cf'>" + "\n");
				sb.append("<div class='stage'>" + listener.getStage(i) + "</div>" + "\n");
				if (listener.getDetail(i) != null) {
					sb.append("<p class='detail'>" + listener.getDetail(i) + "</p>" + "\n");
				}
				sb.append("</div>" + "\n");
			}
		}

		if (terminated) 
			sb.append("<div id='Job Done'></div>" + "\n");

		return sb.toString();
	}	

	/**
	 * Report the progress of a file upload through a web application.
	 * @return	the upload progress as HTML
	 */
	public String uploadProgress() {
		StringBuffer sb = new StringBuffer();

		FileUploadListener listener = null;
		double mbRead = 0, mbTotal = 0;
		listener = session.listener().uploadListener;

		if (listener == null)
			return "";

		mbRead = (double) listener.getBytesRead() / 1024 / 1024;
		mbTotal = (double) listener.getContentLength() / 1024 / 1024;
		double percent = mbRead * 100 / mbTotal;

		mbRead = (double) Math.round(mbRead * 100) / 100;
		mbTotal = (double) Math.round(mbTotal * 100) / 100;
		percent = (double) Math.round(percent * 10) / 10;

		if (mbRead == mbTotal) {
			sb.append("<div class='cf stageHolder firstStageHolder'>" + "\n");
			sb.append("<div class='stage'>Upload</div>" + "\n");
			sb.append("<p class='detail'>Done.</p>" + "\n");
			sb.append("</div>" + "\n");
		} else {
			sb.append("<div class='cf stageHolder firstStageHolder'>" + "\n");
			sb.append("<div class='stage'>Upload</div>" + "\n");
			sb.append("<p class='detail'>" + mbRead + " of " + mbTotal + " MB  " + percent + "% </p>" + "\n");
			sb.append("</div>" + "\n");
		}

		return sb.toString();
	}
	
	/**
	 * Write the report as an HTML page. 
	 * @throws IOException
	 */
	public void write() throws IOException {
		File reportFile = new File(session.dir() + "index.html");
		if (!reportFile.exists())
			reportFile.createNewFile();
		FileWriter fw = new FileWriter(reportFile);
		BufferedWriter bw = new BufferedWriter(fw);

		StringBuffer sb = new StringBuffer();

		String header = writeHeader();
		sb.append(header);

		sb.append("<div class='container'>" + "\n");
		sb.append(html);
		sb.append("</div>" + "\n");

		String footer = writeFooter();
		sb.append(footer);

		bw.write(sb.toString());
		bw.close();
	}
	
	/**
	 * Get the finished report in HTML format.
	 * @return	report as HTML
	 */
	public String html() {
		return html;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void terminate() {
		terminated = true;
	}
	
	public abstract String writeHeader();
	
	public abstract String writeFooter();
	
	public abstract String getReportHtml() throws IOException;
	
}
