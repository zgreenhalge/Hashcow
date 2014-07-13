package gamePieces;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import resourceManager.ImageManager;

public class MapTile {

	private String name;
	private int moveCost;
	private boolean buildable;
	private boolean visible = true;
	private boolean selected;
	private SpriteSheet image;
	private Animation ani;
	private Animation cursor = ImageManager.getAnimation(ImageManager.getSpriteSheet("res/images/selectedTile.png", 32, 32, 1), 400);
	
	public static final MapTile GRASS = new MapTile("Grass", 1, ImageManager.getSpriteSheet("res/images/tiles/grass.png", 32, 32, 0), true);
	private static final Color maskFill = new Color(0.9f, 0.9f, 0.9f, 0.35f);
	
	private MapTile(String n, int cost, SpriteSheet ss, boolean build){
		name = n;
		moveCost = cost;
		image = ss;
		buildable = build;
		ani = new Animation(image, 0, 0, 0, image.getVerticalCount()-1, false, 650, true);
	}
	
	public static MapTile copy(MapTile tile){
		return new MapTile(tile.name, tile.moveCost, tile.image, tile.buildable);
	}
	
	public boolean isTraversable(){
		return moveCost > 0;
	}
	
	public boolean isBuildable(){
		return buildable;
	}
	
	public Animation getAnimation(){
		return ani;
	}
	
	public SpriteSheet getSpriteSheet(){
		return image;
	}
	
	public int moveCost(){
		return moveCost;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean b){
		visible = b;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void select(){
		selected = true;
	}
	
	public void deselect(){
		selected = false;
	}
	
	public void update(int delta){
		ani.update(delta);
		cursor.update(delta);
	}

	public void render(Graphics g, int X, int Y) {
		if(visible){
			ani.draw(X, Y);
			if(selected)
				cursor.draw(X, Y);
		}
		else
			ani.draw(X, Y, maskFill);
	}
	
}
