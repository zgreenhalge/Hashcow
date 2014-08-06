package utils;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time {

	private static GregorianCalendar cal;
	
	public String date(){
	    return String.format("%02d/%02d/%04d", cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
	}

	public String fileDate(){
		return String.format("%02d-%02d-%04d", cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
	}
	
	public String fileDateTime(){
		return String.format("%02d_%02d_%04d %02d-%02d", cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
								cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
	}
	
	public String dateTime(){
	    return date() + " " + currentTime();
	}
	
	public String currentTime(){
	    return String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.AM_PM));
	}
	
	public String currentTimePrecise(){
	    return String.format("%02d:%02d:%02d.%03d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
	}
	public static Time updateCal(){ 
	    cal = new GregorianCalendar();
	    return new Time();			//allows for chaining calls
	}
	
	public static Time setDate(long milliseconds){ 
	    cal.setTimeInMillis(milliseconds);
	    return new Time();			//allows for chaining calls
	}
	
}

