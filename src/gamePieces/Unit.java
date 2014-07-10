package gamePieces;

import interfaceElements.Button;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.UnitImage;
import resourceManager.UnitSound;

public abstract class Unit {

	protected static final TrueTypeFont f = FontManager.TINY_TRUETYPE;
	protected static final Color graveInjuryMask = Color.red;
	protected static final Color injuryMask = Color.magenta; 
	
	protected UnitImage image;
	protected UnitSound sound;
	protected Animation current;
	
	protected MapInfo map;
	protected int column;
	protected int row;
	protected int currentX;
	protected int currentY;
	
	protected Player owner;
	protected boolean visible;
	protected boolean dead;

	protected String name = "Unknown";
	protected int BASE_MOVE_RANGE;
	protected int BASE_SIGHT_RANGE;
	protected int BASE_ATTACK_RANGE;
	protected int BASE_ATTACK;
	protected int BASE_DEFENSE;
	protected int BASE_HEALTH;
	protected int currentHealth;
	protected String healthString;
	
	//movementType
	//attackType
	//abilities
	//upgrades
	
	public Unit(Coordinate loc, Player player){
		column = loc.X();
		row = loc.Y();
		currentX = column*32;
		currentY = row*32;
		owner = player;
		visible = false;
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
	
	public ArrayList<Button> select(Player selector){
		ArrayList<Button> ret = new ArrayList<Button>();
		if(visible){
			current = image.getAnimation(UnitImage.SELECTED);
			sound.playSound(UnitSound.SELECT);
			if(selector.equals(owner)){
				//for(Ability a: abilites)
				//	ret.add(a.getButton());
				//draw move range?
			}else{
				//draw attack range?
			}
		}
		return ret;
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
	
	public String getName(){
		return name;
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
	
	public void setBaseMoveRange(int i){
		BASE_MOVE_RANGE = i;
	}
	
	public void setBaseSightRange(int i){
		BASE_SIGHT_RANGE = i;
	}
	
	public void setBaseAttackRange(int i){
		BASE_ATTACK_RANGE = i;
	}
	
	public void setBaseAttack(int i){
		BASE_ATTACK = i;
	}
	
	public void setBaseDefense(int i){
		BASE_DEFENSE = i;
	}
	
	public void setCurrentHealth(int i){
		if(i>=BASE_HEALTH)
			currentHealth = BASE_HEALTH;
		else if(i<0)
			currentHealth = 0;
		else
			currentHealth = i;
	}
	
	public void setBaseHealth(int i){
		BASE_HEALTH = i;
	}
	
	public void setName(String s){
		name = s;
	}
	
	public void refreshHealthString(){
		healthString = currentHealth + "";
	}
	
	public void update(int delta){
		current.update(delta);
		if(currentHealth == 0 && !dead){
			kill();
			return;
		}
		else if(current.equals(image.getAnimation(UnitImage.DEATH)) && current.isStopped()){
			map.removeUnit(this);
			owner.removeUnit(this);
			return;
		}
		else if(currentX < column*32)
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
		if(visible){
			if(currentHealth <= BASE_HEALTH/5)
				current.draw(X+currentX, Y+currentY, graveInjuryMask);
			else if(currentHealth <= BASE_HEALTH/2)
				current.draw(X+currentX, Y+currentY, injuryMask);
			else if(currentHealth > BASE_HEALTH/2)
				current.draw(X+currentX, Y+currentY);
		}
	}
	
	public boolean isAt(int X, int Y){
		return column == X && row == Y; 
	}
	
	public void moveUp(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(row--, column)));
	}
	
	public void moveDown(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(row++, column)));
	}
	
	public void moveLeft(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(row, column--)));
	}
	
	public void moveRight(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(row, column++)));
	}
	
	public int takeDamage(Unit attacker){
		int damage = attacker.getAttack() - BASE_DEFENSE;
		currentHealth -= damage;
		if(currentHealth < 0)
			currentHealth = 0;
		else if(currentHealth > BASE_HEALTH)
			currentHealth = BASE_HEALTH;
		refreshHealthString();
		return currentHealth;
	}
	
	public void age(){
		
	}
	
	public void setVisible(boolean b){
		visible = b;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Unit))
			return false;
		Unit temp = (Unit) obj;
		if(temp.row == this.row)
			if(temp.column == this.column)
				if(temp.name.equals(this.name))
					return true;
		return false;
	}
	
	@Override
	public int hashCode(){
		return row ^ column ^ name.hashCode();
	}

	public Coordinate getLocation() {
		return new Coordinate(column, row);
	}
}
