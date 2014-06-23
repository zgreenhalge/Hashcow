package utils;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class Logger implements Subject{

	
    private Logger(){}

    public static final int ID = 000;
    
    private static String dateTime;
    private static File logFolder;
    private static File currentLog;
    private static PrintWriter out;
    private static ArrayList<String> writeOut;
    private static boolean dev = false;
    private static GameContainer gc;
    private static ArrayList<PrintStruct> printList;
    private static Font font;
    private static TrueTypeFont ttfont;
    private static int PRINT_LENGTH;
	private static float X;
	private static float Y;
	private static int lineHeight;
	private static ArrayList<Observer> obs;
	private static boolean init = false;

    /**
     * Initializes the Logger with the given root.
     * 
     * @param logDir - File representation of the program's root path
     * @throws IOException when there are IO issues
     */
	public static void init(File logDir, GameContainer container) throws IOException{
		dateTime = Time.updateCal().fileDateTime();
		logDir.mkdirs();
		logFolder = logDir;
		currentLog = new File(logFolder, dateTime + ".txt");
		logFolder.mkdirs();
		currentLog.createNewFile();
		writeOut = new ArrayList<String>();
		writeOut.add("");
		gc = container;
		font = new Font("Times New Roman", Font.PLAIN, 12);
		ttfont = new TrueTypeFont(font, false);
		printList = new ArrayList<PrintStruct>();
		lineHeight = ttfont.getLineHeight();
		PRINT_LENGTH = container.getHeight()/lineHeight;
		X = 0;	//left boundary of print area
		Y = container.getHeight()-(lineHeight*PRINT_LENGTH); //top boundary of print area
		obs = new ArrayList<Observer>();
		init = true;
	}
	
	public static boolean isInit(){
		return init;
	}
	
	/**
	 * Swaps the devMode, either enabling or disabling verbose output through the Logger.
	 * @return boolean value of the current status of dev mode
	 */
	public static boolean isDevEnabled(){
		return dev;
	}
	
	/**
	 * Set the dev mode to a specific value.
	 * @param enabled - true for dev mode on, false for dev mode off
	 */
	public static void setDev(boolean enabled){
		dev = enabled;
	}
	
	/**
	 * Flushes the Standard out and error streams, and writes the Log out to file.
	 * @throws IOException when there is a write error
	 */
	public static void flush() throws IOException{
		writeOut();
	}
	
	public static void setContainer(GameContainer container){
		gc = container;
	}
	
	/**
	 * Writes the log out to file.
	 * @throws IOException when there is a write error
	 */
	public static void writeOut() throws IOException{
		out = new PrintWriter(new BufferedWriter(new FileWriter(currentLog, true)));
		for(String s: writeOut)
			out.println(s);
		out.close();
		writeOut = new ArrayList<String>();
		writeOut.add("");
	}
	
	public static void render(){
		Graphics g = gc.getGraphics();
		Color prevColor = g.getColor();
		org.newdawn.slick.Font prevFont = g.getFont();
		g.setFont(ttfont);
		//g.setColor(Color.darkGray);
		//g.fillRect(X, Y, (float)300, (float)(lineHeight*PRINT_LENGTH)); //paint background
		

		int y = 0;
		int i = printList.size();
		while(y < PRINT_LENGTH && i > 0){
			PrintStruct temp = printList.get(--i);
			if(temp.isException()){
				g.setColor(Color.red);
			}else{
				if(g.getBackground().equals(Color.white))
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
			}
			y += temp.render(g, (int)X, (int)(Y+(lineHeight*(PRINT_LENGTH-1))-(lineHeight*y)));
		}
		g.setColor(prevColor);
		g.setFont(prevFont);
	}

	/**
	 * Silently log the exception. If dev mode is active, all calls are printed. 
	 * @param e - the exception to be logged
	 */
	public static void log(Exception e) {
           if(dev)
                   loudLog(e);
           else{
	           writeOut.add(Time.updateCal().dateTime() + ": " + e.getMessage());
	           if(!e.getLocalizedMessage().equals(e.getMessage()))
	           	writeOut.add(" " + e.getLocalizedMessage());
	           for(StackTraceElement ste: e.getStackTrace()){
	                   writeOut.add("   " + ste.toString());
	           }
	           writeOut.add("#####################################");
	           printList.add(new PrintStruct(e));
		}
	}

	/**
	 * Logs the given input, equivalent to Stream.&nbsp;print(String).&nbsp;
	 * If dev mode is active, all calls are printed.
	 * @param input - the String to be recorded
	 */
	@Deprecated
	public static void log(String input) {
		if(dev) 
            loudLog(input);
		else{
			writeOut.get(writeOut.size()-1).concat(input);
			printList.add(new PrintStruct(input));
		}
	}
	
	/**
	 * Logs the given input as a new line.&nbsp; Equivalent to Stream.&nbsp;println(String).&nbsp;
	 * If dev mode is active, all calls are printed. 
	 * @param input - the String to be recorded
	 */
	public static void logLine(String input){
		if(dev) 
			loudLogLine(input);
		else
			writeOut.add(input);
	}

	/**
	 * Logs and prints the given input, equivalent to Stream.&nbsp;print(String).&nbsp;
	 * @param input - the String to be recorded
	 */
	@Deprecated
	public static void loudLog(String input){
		System.out.print(input);
		writeOut.get(writeOut.size()-1).concat(input);
		printList.add(new PrintStruct(input));
		render();
	}

	/**
	 * Logs the given input.&nbsp; This method will never print out.
	 * @param input
	 */
	public static void logNote(String input){
		writeOut.add(input);
		printList.add(new PrintStruct(input));
	}
	
	public static void streamLog(String input){
		System.out.println(input);
		writeOut.add(input);
	}
	
	/**
	 * Logs and prints the given input, equivalent to Stream.&nbsp;println(String).&nbsp;
	 * @param input - the String to be recorded
	 */
	public static void loudLogLine(String input){
		System.out.println(input);
		writeOut.add(input);
		printList.add(new PrintStruct(input));
		render();
	}
	
	/**
	 * Logs and prints the stack trace of the given exception.
	 * @param e - the Exception to be recorded
	 */
	public static void loudLog(Exception e) {
		System.out.flush();
		System.err.flush();
                writeOut.add("At " + Time.updateCal().dateTime());
		writeOut.add("#####################################");
		writeOut.add("Exception: " + e.getMessage());
		writeOut.add(" " + e.getLocalizedMessage());
		for(StackTraceElement ste: e.getStackTrace()){
			writeOut.add(ste.toString());
		}
		writeOut.add("#####################################");
		PrintStruct temp = new PrintStruct(e);
		System.out.println(temp.getMessage());
		printList.add(temp);
		render();
	}

	@Override
	public void registerObserver(Observer o) {
		obs.add(o);
	}

	@Override
	public void unregisterObserver(Observer o) {
		obs.remove(o);
	}

	@Override
	public void alertObservers() {
		for(Observer o: obs)
			o.alert(ID);
	}

	@Override
	public int getID() {
		return ID;
	}

}
