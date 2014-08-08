package gamePieces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import resourceManager.ImageManager;

/**
 * A terrain Tile that makes up the game map.  
 *
 */
public class Tile implements Selectable, Serializable{

	private static final long serialVersionUID = -5677051974294100077L;
	
	private String name;
	private int[] moveCost;
	private boolean buildable;
	private boolean visible = true;
	private boolean selected;
	private String fileName;
	private transient SpriteSheet image;
	private transient Animation ani;
	private static transient Animation cursor = ImageManager.getAnimation(ImageManager.getSpriteSheet("res/images/selectedTile.png", 32, 32, 1), 400);
	
	private static final Color maskFill = new Color(0.9f, 0.9f, 0.9f, 0.35f);
	
	private Tile(String n, int[] costs, boolean build, String fName){
		name = n;
		moveCost = costs;
		buildable = build;
		fileName = fName;
		load();
	}
	
	public void load(){
		image = ImageManager.getSpriteSheet("res/images/tiles/" + name.toLowerCase() + ".png", 32, 32, 1);
		ani = new Animation(image, 0, 0, 0, image.getVerticalCount()-1, false, 650, true);
	}
	
	public String getFileName(){
		return fileName;
	}
	
	/**
	 * Create a new Tile from one of the static templates in the class.
	 * @param tile - the Tile to create a copy of
	 * @return a new Tile instance with the same values as the passed Tile
	 */
	public static Tile copy(TileTemplate template){
		return new Tile(template.name, template.moveCost, template.buildable, template.fileName);
	}
	
	/**
	 * Check if the Tile is traversable by Units
	 * @return true if the Tile is traversable
	 */
	public boolean isTraversable(){
		return moveCost[0] > 0;
	}
	
	/**
	 * Check if the Tile may be built upon
	 * @return true if the Tile may be built upon
	 */
	public boolean isBuildable(){
		return buildable;
	}
	
	/**
	 * Get the current Animation for the Tile
	 * @return
	 */
	public Animation getAnimation(){
		return ani;
	}
	
	public Image getCurrentFrame(){
		return ani.getCurrentFrame();
	}
	
	/**
	 * Get the move cost of the Tile
	 * @return > 0 if the tile is traversable, -1 if it is not
	 */
	public int moveCost(MovementType type){
		switch(type){
		case IMMOBILE: return moveCost[0];
		case WALK: return moveCost[1];
		case TIRES: return moveCost[2];
		case TREADS: return moveCost[3];
		case HOVER: return moveCost[4];
		default: return 1;
		}
	}
	
	/**
	 * Get the name of the Tile
	 * @return the Tile name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Check if the Tile is currently visible
	 * @return true if it is visible
	 */
	public boolean isVisible(){
		return visible;
	}
	
	/**
	 * Set the tile's visiblity
	 * @param b - the new value of the Tile's visibility
	 */
	public void setVisible(boolean b){
		visible = b;
	}
	
	/**
	 * Check if the Tile is currently selected
	 * @return true if the Tile is selected
	 */
	public boolean isSelected(){
		return selected;
	}
	
	/**
	 * Select the Tile
	 */
	public void select(Coordinate coords, Player selector){
		selected = true;
	}
	
	/**
	 * Deselect the Tile
	 */
	public void deselect(){
		selected = false;
	}
	
	/**
	 * Update the Tile
	 * @param delta - the time since last update
	 */
	public void update(int delta){
		ani.update(delta);
		cursor.update(delta);
	}

	/**
	 * Render the Tile
	 * @param g - the current Graphics object
	 * @param X - the X offset of the map
	 * @param Y - the Y offset of the map
	 */
	public void render(Graphics g, int X, int Y) {
		if(visible){
			ani.draw(X, Y);
			if(selected)
				cursor.draw(X, Y);
		}
		else
			ani.draw(X, Y, maskFill);
	}
	
	/*private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.writeObject(name);
		
		oos.flush();
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		name = (String) ois.readObject();
		switch(name){
		case "Grass": moveCost = TileTemplate.GRASS.moveCost;
			buildable = TileTemplate.GRASS.buildable;
			visible = false;
			selected = false;
			load();
			break;
		}
	}*/
}
