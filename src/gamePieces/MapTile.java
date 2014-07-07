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
	private SpriteSheet image;
	private Animation ani;
	private boolean buildable;
	private boolean visible;
	private static Color pre;
	
	public static final MapTile GRASS = new MapTile("Grass", 1, ImageManager.getSpriteSheet("res/images/tiles/grass.png", 32, 32, 0), true);
	private static final Rectangle mask = new Rectangle(0, 0, 32, 32);
	private static final Color maskFill = new Color(0, 0, 0, 0.7f);
	
	private MapTile(String n, int cost, SpriteSheet ss, boolean build){
		name = n;
		moveCost = cost;
		image = ss;
		buildable = build;
		ani = new Animation(image, 0, 0, 0, image.getVerticalCount()-1, false, 650, true);
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

	public void render(Graphics g, int X, int Y) {
		ani.draw(X, Y);
		if(!visible){
			mask.setX(X);
			mask.setY(Y);
			pre = g.getColor();
			g.setColor(maskFill);
			g.fill(mask);
			g.setColor(pre);
		}
	}
	
	
	
}
