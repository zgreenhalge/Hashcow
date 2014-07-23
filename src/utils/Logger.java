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

public class Logger{

	
    private Logger(){}

    public static final int ID = 000;
    
    private static String dateTime;
    private static File logFolder;
    private static File currentLog;
    private static PrintWriter out;
    private static ArrayList<String> writeOut;
    private static boolean verbose = false;
    private static GameContainer gc;
    private static ArrayList<PrintStruct> printList;
    private static Font font;
    private static TrueTypeFont ttfont;
    private static int PRINT_LENGTH;
	private static float X;
	private static float Y;
	private static int lineHeight;
	private static boolean init = false;
	private static int timeout = 0;
	private static int age = 0;
	private static boolean writeAllowed;

    /**
     * Initializes the Logger with the given root.
     * 
     * @param logDir - File representation of the program's root path
     * @throws IOException when there are IO issues
     */
	public static void init(File logDir, GameContainer container, boolean write) throws IOException{
		dateTime = Time.updateCal().fileDateTime();
		logDir.mkdirs();
		logFolder = logDir;
		currentLog = new File(logFolder, dateTime + ".txt");
		logFolder.mkdirs();
		if(write)
			currentLog.createNewFile();
		writeOut = new ArrayList<String>();
		writeOut.add("");
		gc = container;
		verbose = (Boolean) Settings.getSetting("dev");
		font = new Font("Times New Roman", Font.PLAIN, 12);
		ttfont = new TrueTypeFont(font, false);
		printList = new ArrayList<PrintStruct>();
		lineHeight = ttfont.getLineHeight();
		X = 0;	//left boundary of print area
		PRINT_LENGTH = container.getHeight()/lineHeight;
		Y = container.getHeight()-(lineHeight*PRINT_LENGTH); //top boundary of print area
		writeAllowed = write;
		init = true;
	}
	
	public static boolean isInit(){
		return init;
	}
	
	public static void setTimeout(int i){
		timeout = i;
	}
	
	public static int getTimeout(){
		return timeout;
	}
	
	/**
	 * Swaps the devMode, either enabling or disabling verbose output through the Logger.
	 * @return boolean value of the current status of dev mode
	 */
	public static boolean isVerboseEnabled(){
		return verbose;
	}
	
	/**
	 * Set the dev mode to a specific value.
	 * @param enabled - true for dev mode on, false for dev mode off
	 */
	public static void setVerboseEnabled(boolean enabled){
		verbose = enabled;
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
	public static boolean writeOut() throws IOException{
		if(writeAllowed){
			out = new PrintWriter(new BufferedWriter(new FileWriter(currentLog, true)));
			for(String s: writeOut)
				out.println(s);
			out.close();
			writeOut = new ArrayList<String>();
			writeOut.add("");
			return true;
		}
		return false;
	}
	
	public static void update(int delta){
		PRINT_LENGTH = gc.getHeight()/lineHeight;
		Y = gc.getHeight()-(lineHeight*PRINT_LENGTH); //top boundary of print area
		for(PrintStruct ps: printList)
			ps.tick(delta);
		age += delta;
	}
	
	public static void render(){
		Graphics g = gc.getGraphics();
		Color prevColor = g.getColor();
		org.newdawn.slick.Font prevFont = g.getFont();
		g.setFont(ttfont);
		
		//g.setColor(Color.darkGray);
		//g.fillRect(X, Y, (float)300, (float)(lineHeight*PRINT_LENGTH)); //paint background
		
		int linesPrinted = 0;
		int i = printList.size();
		while(linesPrinted < PRINT_LENGTH && i > 0){
			PrintStruct temp = printList.get(--i);
			if(temp.isException())
				g.setColor(Color.red);
			else if(temp.isNote())
				g.setColor(Color.yellow);
			else{
				if(g.getBackground().equals(Color.white))
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
			}
			linesPrinted += temp.render(g, (int)X, (int)(Y+(lineHeight*(PRINT_LENGTH-1))-(lineHeight*linesPrinted)));				
		}
		g.setColor(prevColor);
		g.setFont(prevFont);
	}

	public static void log(Exception e) {
           if(verbose)
                   loudLog(e);
           else{
	           writeOut.add("["+Time.updateCal().dateTime()+"]: " + e.getMessage());
	           if(!e.getLocalizedMessage().equals(e.getMessage()))
	        	   writeOut.add(" " + e.getLocalizedMessage());
	           for(StackTraceElement ste: e.getStackTrace()){
	               writeOut.add("   " + ste.toString());
	           };
	           printList.add(new PrintStruct(e));
		}
	}
	
	public static void logLine(String input){
		if(verbose) 
			loudLogLine(input);
		else{
			writeOut.add(input);
			printList.add(new PrintStruct(input));
		}
	}
	
	public static void loudLogLine(String input){
		System.out.println(input);
		writeOut.add(input);
		printList.add(new PrintStruct(input));
		age = 0;
	}

	public static void logNote(String input){
		if(verbose)
			loudLogNote(input);
		else{
			writeOut.add(input);
			printList.add(new PrintStruct(input).setNote());
		}
	}
	
	public static void loudLogNote(String input){
		System.out.println(input);
		writeOut.add(input);
		printList.add(new PrintStruct(input).setNote());
		age = 0;
	}
	
	public static void println(String input){
		System.out.println(input);
	}
	
	public static void loudLog(Exception e) {
		System.out.flush();
		System.err.flush();
		writeOut.add("["+Time.updateCal().dateTime()+"]:" + e.getMessage());
		writeOut.add(" " + e.getLocalizedMessage());
		for(StackTraceElement ste: e.getStackTrace()){
			writeOut.add(ste.toString());
		}
		PrintStruct temp = new PrintStruct(e);
		System.out.println(temp.getMessage());
		printList.add(temp);
		age = 0;
	}
	
	public static int getAge() {
		return age;
	}

}
