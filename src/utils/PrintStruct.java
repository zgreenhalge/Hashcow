package utils;

import org.newdawn.slick.Graphics;

public class PrintStruct implements Comparable<PrintStruct>{

	private String message;
	private long timeRecorded;
	private Exception exception;
	private boolean isException;
	private int timeAlive;
	
	public PrintStruct(String s){
		message = s;
		timeRecorded = System.currentTimeMillis();
		isException = false;
		timeAlive = 0;
	}
	
	public PrintStruct(Exception e){
		StackTraceElement top = e.getStackTrace()[0];
		message = e.getMessage() + "   " + top.getFileName()+":"+top.getLineNumber();
		timeRecorded = System.currentTimeMillis();
		exception = e;
		isException = true;
		timeAlive = 0;
	}
	
	public String getMessage(){
		return message;
	}
	
	public long getTime(){
		return timeRecorded;
	}
	
	public boolean isException(){
		return isException;
	}

	public void tick(int delta){
		timeAlive += delta;
	}
	
	public int getAge(){
		return timeAlive;
	}
	
	@Override
	public int compareTo(PrintStruct other) {
		return (int) (this.timeRecorded - other.getTime());
	}
	
	public int render(Graphics g, int x, int y){
		if(isException){
			StackTraceElement[] st = exception.getStackTrace();
			int lh = g.getFont().getLineHeight();
			int num = 0;
			int topY = y-st.length*lh;
			g.drawString("["+Time.setDate(timeRecorded).currentTime() + "]: " + exception.getMessage(), x, topY);
			for(StackTraceElement ste: st){
				num++;
				g.drawString("      " + ste.getFileName()+":"+ste.getLineNumber() + " " + ste.getMethodName(), x, topY+(num*lh));
			}
			return st.length+1;
		}else{
			g.drawString("["+Time.setDate(timeRecorded).currentTime() + "]: " + message, x, y);
			return 1;
		}
	}
	
}
