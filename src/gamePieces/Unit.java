package gamePieces;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.UnitImage;
import resourceManager.UnitSound;

public abstract class Unit {

	private static final TrueTypeFont f = FontManager.TINY_TRUETYPE;
	
	private UnitImage image;
	private UnitSound sound;
	
	private MapInfo map;
	private Animation current;
	private int column;
	private int row;
	private int nextX;
	private int nextY;
	private int targetX;
	private int targetY;
	private String health;
	
	private int owner;
	private boolean visible;
	
	private int moveRange;
	private int sightRange;
	private int attackRange;
	private int attack;
	private int defense;
	private int maxHealth;
	private int currentHealth;
	
	//movementType
	//attackType
	//abilities
	
	public Unit(){}
	
	public UnitImage getImage(){
		return image;
	}
	
	public void kill(){
		current = image.getAnimation(UnitImage.DEATH);
		current.stopAt(current.getFrameCount()-1);
		current.setLooping(false);
		current.start();
		sound.playSound(UnitSound.DEATH);
		//current
	}
	
	public void select(){
		current = image.getAnimation(UnitImage.SELECTED);
		sound.playSound(UnitSound.SELECT);
	}
	
	public void deselect(){
		current = image.getAnimation(UnitImage.IDLE);
	}
	
	public int getX(){
		return column;
	}
	
	public int getY(){
		return row;
	}
	
	public int getMoveRange(){
		return moveRange;
	}
	
	public int getSightRange(){
		return sightRange;
	}
	
	public int getAttackRange(){
		return attackRange;
	}
	
	public int getAttack(){
		return attack;
	}
	
	public int getDefense(){
		return defense;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public int getCurrentHealth(){
		return currentHealth;
	}
	
	public void update(int delta){
		current.update(delta);
		if(currentHealth == 0){
			kill();
			return;
		}
		if(current.isStopped()){
			map.removeUnit(column, row);
			return;
		}
		//movement logic goes here?
	}
	
	public void moveTo(int X, int Y){
		if(Math.abs(X-column) + Math.abs(Y-row) <= moveRange){
			targetX = X;
			targetY = Y;
		}
	}
	
	public boolean isStopped(){
		return current.isStopped();
	}
	
	public void render(Graphics g, int X, int Y){
		if(currentHealth > 0){
			current.draw(X+column*32, Y+row*32);
			g.setFont(f);
			g.drawString(health, X+(column+1)*32-f.getWidth(health), Y+(row+1)*32-f.getLineHeight());
			g.setFont(FontManager.DEFAULT_TRUETYPE);
		}
	}
	
	public int takeDamage(Unit attacker){
		int damage = attacker.getAttack() - defense;
		if(damage > 0)
			currentHealth -= damage;
		if(currentHealth < 0)
			currentHealth = 0;
		health = currentHealth + "/" + maxHealth;
		return currentHealth;
	}
}
