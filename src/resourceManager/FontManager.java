package resourceManager;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class FontManager {

	private static FontManager INSTANCE;
	
	private Font loggerFont;
	private TrueTypeFont loggerTTFont;
	private Font buttonFont;
	private TrueTypeFont buttonTTFont;
	private Font defaultFont;
	private TrueTypeFont defaultTTFont;
	public static final int LOGGER = 0;
	public static final int DEFAULT = 1;
	public static final int BUTTON_FONT = 2;
	
	private FontManager(){
		loggerFont = new Font("Times New Roman", Font.PLAIN, 12);
		loggerTTFont = new TrueTypeFont(loggerFont, false);
		defaultFont = new Font("Verdana", Font.PLAIN, 12);
		defaultTTFont = new TrueTypeFont(defaultFont, false);
		buttonFont = new Font("Times New Roman", Font.ITALIC, 18);
		buttonTTFont = new TrueTypeFont(buttonFont, false);
	}
	
	public static FontManager getManager(){
		if(INSTANCE == null)
			INSTANCE = new FontManager();
		return INSTANCE;
	}
	
	public Font getFont(int id){
		switch(id){
			case LOGGER: return loggerFont;
			case DEFAULT: return defaultFont;
			case BUTTON_FONT: return buttonFont;
			default: return defaultFont;
		}
	}
	
	public TrueTypeFont getFontAsTrueType(int id){
		switch(id){
		case LOGGER: return loggerTTFont;
		case DEFAULT: return defaultTTFont;
		case BUTTON_FONT: return buttonTTFont;
		default: return defaultTTFont;
	}
	}
}
