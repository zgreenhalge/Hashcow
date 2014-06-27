package resourceManager;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import utils.Logger;

public class ImageManager {

	
	public static Image getImage(String path){
		return null;
	}
	
	public static SpriteSheet getSpriteSheet(String path, int frameWidth, int frameHeight, int spacing){
		SpriteSheet ret = null;
		try{ret = new SpriteSheet(path, frameWidth, frameHeight, null, spacing);}
		catch(Exception e){Logger.log(e);}
		return ret;
	}
}
