package resourceManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

import utils.Logger;

public class UnitImage {

	public static final int IDLE = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int MOVE_LEFT = 3;
	public static final int MOVE_RIGHT = 4;
	public static final int ATTACK_RIGHT = 5;
	public static final int ATTACK_LEFT = 6;
	public static final int ATTACK_UP = 7;
	public static final int ATTACK_DOWN = 8;
	public static final int SELECTED = 9;
	public static final int DEATH = 10;
	public static final int INJURED = 11;
	public static final int GRAVELY_INJURED = 12;
	
	private SpriteSheet idle;
	private SpriteSheet moveUp;
	private SpriteSheet moveDown;
	private SpriteSheet moveLeft;
	private SpriteSheet moveRight;
	private SpriteSheet attackRight;
	private SpriteSheet attackLeft;
	private SpriteSheet attackUp;
	private SpriteSheet attackDown;
	private SpriteSheet death;
	private Animation idleAni;
	private Animation upAni;
	private Animation downAni;
	private Animation leftAni;
	private Animation rightAni;
	private Animation attR;
	private Animation attL;
	private Animation attU;
	private Animation attD;
	private Animation select;
	private Animation deathAni;
	private Animation injuryAni;
	private Animation graveInjuryAni;
	
	
	public UnitImage(String unitDir) throws Exception{
		idle = new SpriteSheet(unitDir+"/idle.png", 32, 32, null, 1); //if this file won't load, don't bother loading the rest of them
		
		try {moveUp = new SpriteSheet(unitDir+"/moveUp.png", 32, 32, null, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveUp = idle;
		}
		try {moveDown = new SpriteSheet(unitDir+"/moveDown.png", 32, 32, null, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveDown = idle;
		}
		try {moveLeft = new SpriteSheet(unitDir+"/moveLeft.png", 32, 32, null, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveLeft = idle;
		}
		try {moveRight = new SpriteSheet(unitDir+"/moveRight.png", 32, 32, null, 1);}
		catch (Exception e) {
			Logger.log(e);
			moveRight = idle;
		}
		try{attackRight = new SpriteSheet(unitDir+"/attackRight.png", 32, 32, null, 1);}
		catch(Exception e){
			Logger.log(e);
			attackRight = idle;
		}
		try{attackLeft = new SpriteSheet(unitDir+"/attackLeft.png", 32, 32, null, 1);}
		catch(Exception e){
			Logger.log(e);
			attackLeft = idle;
		}
		try{attackUp = new SpriteSheet(unitDir+"/attackUp.png", 32, 32, null, 1);}
		catch(Exception e){
			Logger.log(e);
			attackUp = idle;
		}
		try{attackDown = new SpriteSheet(unitDir+"/attackDown.png", 32, 32, null, 1);}
		catch(Exception e){
			Logger.log(e);
			attackDown = idle;
		}
		try{death = new SpriteSheet(unitDir+"/death.png", 32, 32, null, 1);}
		catch(Exception e){
			Logger.log(e);
			death = idle;
		}
	}
	
	public Animation getAnimation(int direction){
		switch(direction){
			case IDLE: if(idleAni == null)
						idleAni = buildAnimation(idle);
					return idleAni;
			case MOVE_UP: if(upAni == null)
						upAni = buildAnimation(moveUp);
					return upAni;
			case MOVE_DOWN: if(downAni == null)
						downAni = buildAnimation(moveDown);
					return downAni;
			case MOVE_LEFT: if(leftAni == null)
						leftAni = buildAnimation(moveLeft);
					return leftAni;
			case MOVE_RIGHT: if(rightAni == null)
							rightAni = buildAnimation(moveRight);
					return rightAni;
			case ATTACK_RIGHT: if(attR == null)
									attR = buildAnimation(attackRight);
					return attR;
			case ATTACK_LEFT: if(attL == null)
								attL = buildAnimation(attackLeft);
					return attL;
			case ATTACK_UP: if(attU == null)
								attU = buildAnimation(attackUp);
					return attU;
			case ATTACK_DOWN: if(attD == null)
							attD = buildAnimation(attackDown);
					return attD;
			case SELECTED: if(select == null)
							select = new Animation(idle, 0, 0, 0, idle.getVerticalCount()-1, false, 300, true);
					return select;
			case DEATH: if(death == null)
							deathAni = buildAnimation(death);
					return deathAni;
			case INJURED: if(injuryAni == null)
							injuryAni = new Animation(idle, 0, 0, 0, idle.getVerticalCount()-1, false, 650, true);
					return injuryAni;
			case GRAVELY_INJURED: if(graveInjuryAni == null)
							graveInjuryAni = new Animation(idle, 0, 0, 0, idle.getVerticalCount()-1, false, 800, true);
					return graveInjuryAni;
			default: return getAnimation(IDLE);
		}
		
	}
	
	private Animation buildAnimation(SpriteSheet spr){
		return new Animation(spr, 0, 0, 0, spr.getVerticalCount()-1, false, 500, true);
	}
	
}
