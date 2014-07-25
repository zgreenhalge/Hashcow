package gamePieces;
 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue; 
import java.util.Set;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.ImageManager;
import resourceManager.TestUnitLibrary;
import resourceManager.UnitImage;
import resourceManager.UnitSound;
import utils.OneToOneMap;

/**
 * A generic Unit
 *
 */
public abstract class Unit implements Selectable, Serializable{

	private static final long serialVersionUID = 1896671301456843011L;
	
	protected static final TrueTypeFont f = FontManager.TINY_TRUETYPE;
	protected static final Color graveInjuryMask = Color.red;
	protected static final Color injuryMask = Color.magenta; 
	
	protected transient UnitImage image;
	protected transient UnitSound sound;
	protected transient Animation current;
	protected transient Animation cursor = ImageManager.getAnimation(ImageManager.getSpriteSheet("res/images/selectedTile.png", 32, 32, 1), 400);
	protected int unitId;
	
	protected MovementType moveType;
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
	protected int baseMoveRange;
	protected int baseSightRange;
	protected int baseAttackRange;
	protected int baseAttack;
	protected int baseDefense;
	protected int baseHealth;
	protected int currentHealth;
	
	//movementType
	//attackType
	//abilities
	//upgrades
	
	public Unit(Coordinate loc, Player player, MapInfo map, MovementType move){
		location = loc;
		currentX = location.X()*32;
		currentY = location.Y()*32;
		owner = player;
		moveType = move;
		visible = false;
		this.map = map;
	}
	
	public void load(){
		image = TestUnitLibrary.getUnitImage(unitId);
		sound = TestUnitLibrary.getUnitSound(unitId);
		current = image.getAnimation(UnitImage.IDLE);
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
				current.getCurrentFrame().setAlpha(0.75f);
			else
				current.getCurrentFrame().setAlpha(1.0f);
			current.draw(X+currentX, Y+currentY, owner.getColor());
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
	 * Get the Animation that the Unit is currently rendering
	 * @return the Unit's current Animation
	 */
	public Animation getCurrentAnimation(){
		return current;
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
	
	public MovementType getMovementType(){
		return moveType;
	}

	public String getName(){
		return name;
	}
	
	public int getBaseMoveRange(){
		return baseMoveRange;
	}
	
	public int getBaseSightRange(){
		return baseSightRange;
	}

	public int getBaseAttackRange(){
		return baseAttackRange;
	}
	

	public int getBaseAttack(){
		return baseAttack;
	}
	
	public int getBaseDefense(){
		return baseDefense;
	}
	
	public int getMaxHealth(){
		return baseHealth;
	}
	
	public int getCurrentHealth(){
		return currentHealth;
	}
	
	public void setBaseMoveRange(int i){
		baseMoveRange = i;
	}
	
	public void setBaseSightRange(int i){
		baseSightRange = i;
	}
	
	public void setBaseAttackRange(int i){
		baseAttackRange = i;
	}
	
	public void setBaseAttack(int i){
		baseAttack = i;
	}
	
	public void setBaseDefense(int i){
		baseDefense = i;
	}
	
	public void setCurrentHealth(int i){
		if(i>=baseHealth)
			currentHealth = baseHealth;
		else if(i<0)
			currentHealth = 0;
		else
			currentHealth = i;
	}
	
	public void setBaseHealth(int i){
		baseHealth = i;
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
		else if(currentHealth > baseHealth)
			currentHealth = baseHealth;
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
		if(temp.currentX == this.currentX)
			if(temp.currentY == this.currentY)
				return true;
		return false;
	}
	
	@Override
	public int hashCode(){
		return currentX ^ currentY;
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

	public Player getOwner() {
		return owner;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.writeInt(unitId);		
		oos.writeObject(map);
		oos.writeObject(location);
		oos.writeInt(currentX);
		oos.writeInt(currentY);
		oos.writeObject(owner);
		oos.writeBoolean(selected);
		oos.writeBoolean(visible);
		oos.writeBoolean(dead);
		oos.writeBoolean(moveDisplay);
		oos.writeBoolean(attackDisplay);
		oos.writeObject(name);
		oos.writeInt(baseMoveRange);
		oos.writeInt(baseSightRange);
		oos.writeInt(baseAttackRange);
		oos.writeInt(baseAttack);
		oos.writeInt(baseDefense);
		oos.writeInt(baseHealth);
		oos.writeInt(currentHealth);
		
		oos.flush();
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
		unitId = ois.readInt();
		map = (MapInfo) ois.readObject();
		location = (Coordinate) ois.readObject();
		currentX = ois.readInt();
		currentY = ois.readInt();
		owner = (Player) ois.readObject();
		selected = ois.readBoolean();
		visible  = ois.readBoolean();
		dead = ois.readBoolean();
		moveDisplay = ois.readBoolean();
		attackDisplay = ois.readBoolean();
		name = (String) ois.readObject();
		baseMoveRange = ois.readInt();
		baseSightRange = ois.readInt();
		baseAttackRange = ois.readInt();
		baseAttack = ois.readInt();
		baseDefense = ois.readInt();
		baseHealth = ois.readInt();
		currentHealth = ois.readInt();
	}
	
	
	/**
	 * A pathfinding struct to point to all places in a Unit's path
	 * @author Anthony
	 */
	class PathStruct{
		PathStruct prev;
		PathStruct next;
		Coordinate coord;
		int pathRemaining;
		 
		public PathStruct(PathStruct pre, Coordinate c)
		{
			prev = pre;
			coord = c;
			if(prev == null)
				pathRemaining = getBaseMoveRange();
			else
				pathRemaining = prev.pathRemaining;
		}
		
		public PathStruct getPrevious(){
			return prev;
		}
		
		
		/**
		 * Determine the next Coordinate in the path
		 * @return
		 */
		public PathStruct determineNext(){
			PathStruct pa = null;
			OneToOneMap<Tile, Coordinate> spaces = map.getAdjacentTiles(getLocation());
			Set<Tile> tiles = spaces.keySet();
			Tile[] t = tiles.toArray(new Tile[0]);
			for(Tile tile : t)
				if(tile.isTraversable())
					if(tile.moveCost(moveType) <= pathRemaining){
						pathRemaining -= tile.moveCost(moveType);
						pa = new PathStruct(PathStruct.this, spaces.getValue(tile));
					}
			return pa;
			}
		 	
		}
}