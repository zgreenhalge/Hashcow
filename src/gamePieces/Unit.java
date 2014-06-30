package gamePieces;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.UnitImage;

public abstract class Unit {

	private static final TrueTypeFont f = FontManager.TINY_TRUETYPE;
	
	private UnitImage image;
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
	
	public Unit(){}
	
	public UnitImage getImage(){
		return image;
	}
	
	public int getCurrentX(){
		return column;
	}
	
	public int getCurrentY(){
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
		//movement logic goes here?
	}
	
	public void render(Graphics g, int X, int Y){
		current.draw(X+column*32, Y+row*32);
		g.setFont(f);
		g.drawString(health, X+(column+1)*32-f.getWidth(health), Y+(row+1)*32-f.getLineHeight());
		g.setFont(FontManager.DEFAULT_TRUETYPE);
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
