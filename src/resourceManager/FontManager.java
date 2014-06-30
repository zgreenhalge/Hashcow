package resourceManager;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class FontManager {
	
	public static final Font LOGGER_FONT = new Font("Rod", Font.PLAIN, 12);
	public static final TrueTypeFont LOGGER_TRUETYPE = new TrueTypeFont(LOGGER_FONT, false);
	public static final Font BUTTON_FONT = new Font("SimHei", Font.PLAIN, 18);
	public static final TrueTypeFont BUTTON_TRUETYPE = new TrueTypeFont(BUTTON_FONT, false);
	public static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 12);
	public static final TrueTypeFont DEFAULT_TRUETYPE = new TrueTypeFont(DEFAULT_FONT, false);
	
	private FontManager(){}
	
}
