package ca.mcmaster.magarveylab.wasp.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Time utility imported from iSNAP.
 * @author Lian
 * 
 */
public class TimeUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH-mm-ss";

	/**
	 * generate time tag with current time/date
	 * @return	time tag with current time/date
	 */
	public static String getTimeTag() {
		Calendar calender = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(calender.getTime());
	}

	public static Date parseTimeTag(String timeTag) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date date = (Date) formatter.parse(timeTag);
		return date;
	}

	/**
	 * return the number of seconds that has past since lastTime
	 * 
	 * @param lastTime
	 * @return
	 * @throws ParseException 
	 */
	public static int timeDiff(String lastTime) throws ParseException {
		Calendar calender = Calendar.getInstance();
		
		Date cur = calender.getTime();
		Date last = TimeUtil.parseTimeTag(lastTime);
		long curLong = cur.getTime();
		long lastLong = last.getTime();

		return (int) ((curLong - lastLong) / 1000);

	}

}
