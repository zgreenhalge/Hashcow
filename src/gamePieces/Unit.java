package gamePieces;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.ImageManager;
import resourceManager.UnitImage;
import resourceManager.UnitSound;

/**
 * A generic Unit
 *
 */
public abstract class Unit implements Selectable{

	protected static final TrueTypeFont f = FontManager.TINY_TRUETYPE;
	protected static final Color graveInjuryMask = Color.red;
	protected static final Color injuryMask = Color.magenta; 
	
	protected UnitImage image;
	protected UnitSound sound;
	protected Animation current;
	protected Animation cursor;
	
	protected MapInfo map;
	protected Coordinate location;
	protected int currentX;
	protected int currentY;
	
	protected Player owner;
	protected boolean selected;
	protected boolean visible;
	protected boolean dead;
	protected boolean moveDisplay;
	protected boolean attackDisplay;

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
	
	public Unit(Coordinate loc, Player player, MapInfo map){
		location = loc;
		currentX = location.X()*32;
		currentY = location.Y()*32;
		owner = player;
		visible = false;
		this.map = map;
		cursor = ImageManager.getAnimation(ImageManager.getSpriteSheet("res/images/selectedTile.png", 32, 32, 1), 400);
	}
	

	
	/**
	 * Update the Unit
	 * @param delta - time since the last update call
	 */
	public void update(int delta){
		current.update(delta);
		cursor.update(delta);
		if(currentHealth == 0 && !dead){
			kill();
			return;
		}
		else if(current.equals(image.getAnimation(UnitImage.DEATH)) && current.isStopped()){
			map.removeUnit(this);
			owner.removeUnit(this);
			return;
		}
		else if(currentX < location.X()*32)
			currentX += 4;
		else if(currentX > location.X()*32)
			currentX -= 4;
		else if(currentY < location.Y()*32)
			currentY += 4;
		else if(currentY > location.Y()*32)
			currentY -= 4;
		//movement logic goes here?
	}
	
	/**
	 * Render the Unit
	 * @param g - the Graphics object being used
	 * @param X - the X offset of the map
	 * @param Y - the Y offset of the map
	 */
	public void render(Graphics g, int X, int Y){
		if(visible){
			if(map.isBuilt(location))
				current.getCurrentFrame().setAlpha(0.5f);
			else
				current.getCurrentFrame().setAlpha(1.0f);
			if(currentHealth <= BASE_HEALTH/5)
				current.draw(X+currentX, Y+currentY, graveInjuryMask);
			else if(currentHealth <= BASE_HEALTH/2)
				current.draw(X+currentX, Y+currentY, injuryMask);
			else if(currentHealth > BASE_HEALTH/2)
				current.draw(X+currentX, Y+currentY);
			if(selected)
				cursor.draw(X+currentX, Y+currentY);
		}
	}
	
	/**
	 * Get the Unit's associated UnitImage
	 * @return the UnitImage that this Unit uses
	 */
	public UnitImage getImage(){
		return image;
	}
	
	/**
	 * Register this Unit with both it's owner and the game board
	 */
	public void register(){
		map.addUnit(this);
		owner.addUnit(this);
	}
	
	/**
	 * Start this unit's death animation and mark it for clean up
	 */
	public void kill(){
		dead = true;
		current = image.getAnimation(UnitImage.DEATH);
		current.stopAt(current.getFrameCount()-1);
		current.setLooping(false);
		current.start();
		sound.playSound(UnitSound.DEATH);
		//current
	}
	
	/**
	 * Mark this Unit as selected
	 * @param selector - the Player who selected the Unit
	 * @return @return ArrayList<Button> - the Buttons for this Unit's abilities 
	 */
	public void select(Coordinate coords, Player selector){
		//ArrayList<Button> ret = new ArrayList<Button>();
		selected = true;
		if(visible){
			current = image.getAnimation(UnitImage.SELECTED);
			sound.playSound(UnitSound.SELECT);
			if(selector.equals(owner)){
				//for(Ability a: abilites)
				//	ret.add(a.getButton());
			}else{
				//draw attack range?
			}
		}
		//return ret;
	}
	
	/**
	 * Deselect this Unit
	 */
	public void deselect(){
		selected = false;
		current = image.getAnimation(UnitImage.IDLE);
	}
	
	public boolean isSelected(){
		return selected;
	}

	public int getColumn(){
		return location.X();
	}

	public int getRow(){
		return location.Y();
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
	
	public int getBaseMoveRange(){
		return BASE_MOVE_RANGE;
	}
	
	public int getBaseSightRange(){
		return BASE_SIGHT_RANGE;
	}

	public int getBaseAttackRange(){
		return BASE_ATTACK_RANGE;
	}
	

	public int getBaseAttack(){
		return BASE_ATTACK;
	}
	
	public int getBaseDefense(){
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
	
	
	/**
	 * Move the Unit one tile up
	 */
	public void moveUp(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(location.Y(-1), location.X())));
	}
	
	/**
	 * Move the Unit one tile down
	 */
	public void moveDown(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(location.Y(1), location.X())));
	}
	
	/**
	 * Move the Unit one tile left
	 */
	public void moveLeft(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(location.Y(), location.X(-1))));
	}
	
	/**
	 * Move the Unit one tile right
	 */
	public void moveRight(){
		map.applySightMap(map.getSightMap().updateUnit(this, new Coordinate(location.Y(), location.X(1))));
	}
	
	/**
	 * Reduce the Unit's health by a flat amount. This method is not affected by defense.
	 * @param damage - the damage the Unit should take
	 * @return the Unit's new health value
	 */
	public int takeDamage(int damage){
		currentHealth -= damage;
		if(currentHealth < 0)
			currentHealth = 0;
		else if(currentHealth > BASE_HEALTH)
			currentHealth = BASE_HEALTH;
		return currentHealth;
	}
	
	/**
	 * Age the Unit by one turn
	 */
	public void age(){
		
	}
	
	/**
	 * Change the Unit's visible status
	 * @param b - the new visibility status of the Unit
	 */
	public void setVisible(boolean b){
		visible = b;
	}
	
	/**
	 * Check if the Unit is visible
	 * @return true if the Unit is visible
	 */
	public boolean isVisible(){
		return visible;
	}
	
	/**
	 * Get the Unit's current location
	 * @return Coordinate location of the Unit
	 */
	public Coordinate getLocation() {
		return new Coordinate(location.X(), location.Y());
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Unit))
			return false;
		Unit temp = (Unit) obj;
		if(temp.location.Y() == this.location.Y())
			if(temp.location.X() == this.location.X())
				if(temp.name.equals(this.name))
					return true;
		return false;
	}
	
	@Override
	public int hashCode(){
		return location.Y() ^ location.X() ^ name.hashCode();
	}

	/**
	 * Get Coordinate locations of all visible tiles to the Unit
	 * @return ArrayList<Coordinate> - all Tiles visible to the Unit
	 */
	public ArrayList<Coordinate> getSight(){
		ArrayList<Coordinate> temp = new ArrayList<Coordinate>();
		int range = getBaseSightRange();
		for(int x = location.X()-range; x <= location.X()+range; x++)
			for(int y = location.Y()-(range-Math.abs(location.X()-x)); y <= location.Y()+(range-Math.abs(location.X()-x)); y++){
				if(y >= 0 && x >= 0 && y < map.getHeight() && x < map.getWidth())
					temp.add(new Coordinate(x,y));
			}
		return temp;
	}

	public MapInfo getMap() {
		return map;
	}

	public void toggleDisplayMove() {
		moveDisplay = !moveDisplay;
	}
	
	public void toggleDisplayAttack(){
		attackDisplay = !attackDisplay;
	}

	public ArrayList<Coordinate> getMovementRange() {
		ArrayList<Coordinate> ret = new ArrayList<Coordinate>();
		
		return ret;
	}
	
	public void moveTo(Coordinate coord){
		
	}
	
}
