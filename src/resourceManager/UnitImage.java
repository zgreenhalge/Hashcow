package resourceManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

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
	
	
	public UnitImage(String unitDir) throws Exception{
		idle = new SpriteSheet(unitDir+"/idle.png", 32, 32, null, 1); //if this file won't load, don't bother loading the rest of them
		Texture tx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(unitDir+"/idle.png"));
		//tx.bind();
		idle.setTexture(tx);
		try {
			moveUp = new SpriteSheet(unitDir+"/moveUp.png", 32, 32, null, 1);
			tx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(unitDir+"/moveUp.png"));
			tx.bind();
			moveUp.setTexture(tx);
		}
		catch (Exception e) {
			Logger.log(e);
			moveUp = idle;
		}
		try {
			moveDown = new SpriteSheet(unitDir+"/moveDown.png", 32, 32, null, 1);
			tx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(unitDir+"/moveDown.png"));
			tx.bind();
			moveDown.setTexture(tx);
		}
		catch (Exception e) {
			Logger.log(e);
			moveDown = idle;
		}
		try {
			moveLeft = new SpriteSheet(unitDir+"/moveLeft.png", 32, 32, null, 1);
			tx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(unitDir+"/moveLeft.png"));
			tx.bind();
			moveLeft.setTexture(tx);
		}
		catch (Exception e) {
			Logger.log(e);
			moveLeft = idle;
		}
		try {
			moveRight = new SpriteSheet(unitDir+"/moveRight.png", 32, 32, null, 1);
			tx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(unitDir+"/moveRight.png"));
			tx.bind();
			moveRight.setTexture(tx);
		}
		catch (Exception e) {
			Logger.log(e);
			moveRight = idle;
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
		return new Animation(spr, 0, 0, 0, spr.getVerticalCount()-1, false, 500, true);
	}
	
}
