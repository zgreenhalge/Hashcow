package resourceManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import utils.Logger;

public class UnitImage {

	public static final int IDLE = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	
	private SpriteSheet idle;
	private SpriteSheet moveUp;
	private SpriteSheet moveDown;
	private SpriteSheet moveLeft;
	private SpriteSheet moveRight;
	private Animation idleAni;
	private Animation upAni;
	private Animation downAni;
	private Animation leftAni;
	private Animation rightAni;
	
	
	public UnitImage(String unitDir) throws SlickException{
		idle = new SpriteSheet(unitDir+"/idle.jpg", 32, 32, Color.white, 1); //if this file won't load, don't bother loading the rest of them
		
		try {moveUp = new SpriteSheet(unitDir+"/moveUp.jpg", 32, 32, Color.white, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveUp = new SpriteSheet(idle, 32, 32);
		}
		try {moveDown = new SpriteSheet(unitDir+"/moveDown.jpg", 32, 32, Color.white, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveDown = new SpriteSheet(idle, 32, 32);
		}
		try {moveLeft = new SpriteSheet(unitDir+"/moveLeft.jpg", 32, 32, Color.white, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveLeft = new SpriteSheet(idle, 32, 32);
		}
		try {moveRight = new SpriteSheet(unitDir+"/moveRight.jpg", 32, 32, Color.white, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveRight = new SpriteSheet(idle, 32, 32);
		}
	}
	
	public Animation getAnimation(int direction){
		switch(direction){
			case IDLE: if(idleAni == null)
						idleAni = buildAnimation(idle);
					return idleAni;
			case UP: if(upAni == null)
						upAni = buildAnimation(moveUp);
					return upAni;
			case DOWN: if(downAni == null)
						downAni = buildAnimation(moveDown);
					return downAni;
			case LEFT: if(leftAni == null)
						leftAni = buildAnimation(moveLeft);
					return leftAni;
			case RIGHT: if(rightAni == null)
							rightAni = buildAnimation(moveRight);
					return rightAni;
			default: return getAnimation(IDLE);
		}
		
	}
	
	private Animation buildAnimation(SpriteSheet spr){
		return new Animation(spr, 0, 0, 0, spr.getVerticalCount()-1, false, 300, true);
	}
	
}
