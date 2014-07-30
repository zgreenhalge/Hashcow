package resourceManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import utils.Logger;

public class ImageManager {

	
	public static Image getImage(String path){
		Image ret = null;
		try {
			ret = new Image(5, 5);
			ret.setImageColor(250, 0, 150);
		} catch (SlickException e) {
			Logger.loudLog(e);
		}
		try {
			ret = new Image(path);
		} catch (SlickException e) {
			Logger.loudLog(e);
		}
		return ret;
	}
	
	public static Animation getAnimation(SpriteSheet ss, int duration){
		return new Animation(ss, duration);
	}
	
	public static SpriteSheet getSpriteSheet(String path, int frameWidth, int frameHeight, int spacing){
		SpriteSheet ret = null;
		try{ret = new SpriteSheet(path, frameWidth, frameHeight, null, spacing);}
		catch(Exception e){e.printStackTrace();Logger.log(e);}
		return ret;
	}
}
