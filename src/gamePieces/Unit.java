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
	private int currentX;
	private int currentY;
	private String healthString;
	
	private int owner;
	private boolean visible;
	private boolean dead;
	
	private int BASE_MOVE_RANGE;
	private int BASE_SIGHT_RANGE;
	private int BASE_ATTACK_RANGE;
	private int BASE_ATTACK;
	private int BASE_DEFENSE;
	private int BASE_HEALTH;
	private int currentHealth;
	
	//movementType
	//attackType
	//abilities
	//upgrades
	
	public Unit(int X, int Y, int player){
		column = X;
		row = Y;
		currentX = column*32;
		currentY = row*32;
		owner = player;
	}
	
	public UnitImage getImage(){
		return image;
	}
	
	public void kill(){
		dead = true;
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
	
	public int getColumn(){
		return column;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getX(){
		return currentX;
	}
	
	public int getY(){
		return currentY;
	}
	
	public int getMoveRange(){
		return BASE_MOVE_RANGE;
	}
	
	public int getSightRange(){
		return BASE_SIGHT_RANGE;
	}
	
	public int getAttackRange(){
		return BASE_ATTACK_RANGE;
	}
	
	public int getAttack(){
		return BASE_ATTACK;
	}
	
	public int getDefense(){
		return BASE_DEFENSE;
	}
	
	public int getMaxHealth(){
		return BASE_HEALTH;
	}
	
	public int getCurrentHealth(){
		return currentHealth;
	}
	
	public void update(int delta){
		current.update(delta);
		if(currentHealth == 0 && !dead){
			kill();
			return;
		}
		if(current.isStopped()){
			map.removeUnit(column, row);
			return;
		}
		if(currentX < column*32)
			currentX += 4;
		else if(currentX > column*32)
			currentX -= 4;
		else if(currentY < row*32)
			currentY += 4;
		else if(currentY > row*32)
			currentY -= 4;
		//movement logic goes here?
	}
	
	public boolean isAnimationStopped(){
		return current.isStopped();
	}
	
	public boolean isWalking(){
		if(currentX != column*32 || currentY != row*32)
			return true;
		return false;
	}
	
	public void render(Graphics g, int X, int Y){
		if(currentHealth > 0){
			current.draw(X+currentX, Y+currentY);
			g.setFont(f);
			g.drawString(healthString, X+(column+1)*32-f.getWidth(healthString), Y+(row+1)*32-f.getLineHeight());
			g.setFont(FontManager.DEFAULT_TRUETYPE);
		}
	}
	
	public boolean isAt(int X, int Y){
		return column == X && row == Y; 
	}
	
	public void moveUp(){
		map.getSightMap().updateUnit(this, row--, column);
	}
	
	public void moveDown(){
		map.getSightMap().updateUnit(this, row++, column);
	}
	
	public void moveLeft(){
		map.getSightMap().updateUnit(this, row, column--);
	}
	
	public void moveRight(){
		map.getSightMap().updateUnit(this, row, column++);
	}
	
	public int takeDamage(Unit attacker){
		int damage = attacker.getAttack() - BASE_DEFENSE;
		if(damage > 0)
			currentHealth -= damage;
		if(currentHealth < 0)
			currentHealth = 0;
		healthString = currentHealth + "/" + BASE_HEALTH;
		return currentHealth;
	}
}
