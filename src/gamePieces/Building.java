package gamePieces;

import java.util.TreeSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import resourceManager.UnitImage;
import utils.Logger;

public class Building extends Unit {

	private static final Color fogMask = new Color(0.9f, 0.9f, 0.9f, 0.35f);
	private TreeSet<Integer> seenBy;
	private int HEIGHT;
	private int WIDTH;
	private boolean[][] pathable;
	private boolean globallyVisible;
	
	public Building(Coordinate location, Player owner, MapInfo map) {
		super(location, owner, map);
		super.BASE_MOVE_RANGE = 0;
		super.BASE_SIGHT_RANGE = 1;
		super.BASE_ATTACK_RANGE = 0;
		super.BASE_ATTACK = 0;
		super.BASE_DEFENSE = 5;
		super.BASE_HEALTH = 50;
		super.currentHealth = 50;
		super.name = "TestHQ";
		HEIGHT = 1;
		WIDTH = 1;
		seenBy = new TreeSet<Integer>();
		pathable = new boolean[WIDTH][HEIGHT];
	}
	
	@Override
	public void update(int delta){
		current.update(delta);
		if(currentHealth == 0 && !dead){
			kill();
			return;
		}
		else if(current.equals(image.getAnimation(UnitImage.DEATH)) && current.isStopped()){
			map.removeUnit(this);
			owner.removeBuilding(this);
			return;
		}
	}
	
	@Override
	public void render(Graphics g, int X, int Y){
		if((visible || globallyVisible) && selected)
			cursor.draw(X+currentX, Y+currentY);
		if(visible){
			current.draw(X+currentX, Y+currentY);
			seenBy.add(map.getSightMap().getId());
		}else if(globallyVisible || seenBy.contains(map.getSightMap().getId())){
			current.getCurrentFrame().draw(X+currentX, Y+currentY, fogMask);
		}
	}

	public int getWidth(){
		return WIDTH;
	}
	
	public int getHeight(){
		return HEIGHT;
	}

	public boolean isPathable(Coordinate coord) {
		return pathable[coord.X()][coord.Y()];
	}
	
	public void setSeenBy(int id){
		seenBy.add(id);
	}
	
	public void setGloballyVisible(boolean b){
		globallyVisible = b;
	}
}
