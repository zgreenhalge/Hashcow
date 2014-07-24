package resourceManager;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class FontManager {
	
	public static final Font LOGGER_FONT = new Font("Miriam Fixed", Font.PLAIN, 12);
	public static final TrueTypeFont LOGGER_TRUETYPE = new TrueTypeFont(LOGGER_FONT, false);
	public static final Font BUTTON_FONT = new Font("SimHei", Font.PLAIN, 18);
	public static final TrueTypeFont BUTTON_TRUETYPE = new TrueTypeFont(BUTTON_FONT, false);
	public static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 12);
	public static final TrueTypeFont DEFAULT_TRUETYPE = new TrueTypeFont(DEFAULT_FONT, false);
	public static final Font TINY_FONT = new Font("Kartika", Font.PLAIN, 8);
	public static final TrueTypeFont TINY_TRUETYPE = new TrueTypeFont(TINY_FONT, false);
	public static final Font NAME_FONT = new Font("SimHei", Font.PLAIN, 26);
	public static final TrueTypeFont NAME_TRUETYPE = new TrueTypeFont(NAME_FONT, false);
	
	private FontManager(){}
	
}
