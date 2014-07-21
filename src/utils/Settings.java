package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Settings {

	private static Scanner scan;
	private static PrintWriter out;
	
	public static final String VOLUME = "volume";
	public static final String DEV_MODE = "dev";
	public static final String RESOLUTION = "resolution";
	public static final String FULLSCREEN = "fullscreen";
	
	private static String volume = "0.25f";
	private static String dev = "true";
	private static String resolution = "1024x1280";
	private static String fullscreen = "false";
	
	private Settings(){}
	
	public static void setToDefaults(){
		volume = "0.25f";
		dev = "true";
		resolution = "1024x1280";
		fullscreen = "false";
	}
	
	public static void loadSettings(String path) throws FileNotFoundException{
		File file = new File(path);
		String[] temp;
		scan = new Scanner(new BufferedReader(new FileReader(file)));
		if(file.exists() && file.isFile()){
			while(scan.hasNext()){
				temp = scan.nextLine().split(":");
				if(temp.length == 2)
					set(temp[0], temp[1]);
			}
			scan.close();
		}
	}
	
	public static void save(String path) throws IOException{
		File file = new File(path);
		out = new PrintWriter(new FileWriter(file));
		out.println("volume:"+volume);
		out.println("dev:"+dev);
		out.println("resolution:"+resolution);
		out.println("fullscreen:"+fullscreen);
		out.close();
	}
	
	public static Object getSetting(String name){
		switch(name){
			case VOLUME: return Float.parseFloat(volume);
			case DEV_MODE: return Boolean.parseBoolean(dev);
			case RESOLUTION: return resolution;
			case FULLSCREEN: return Boolean.parseBoolean(fullscreen);
			default: return null;
		}
	}
	
	
	public static void set(String name, String value){
		switch(name){
			case VOLUME: volume = value; break;
			case DEV_MODE: dev = value; break;
			case RESOLUTION: resolution = value; break;
			case FULLSCREEN: fullscreen = value; break;
			default: break;
		}
	}
}
