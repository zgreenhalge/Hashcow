package gamePieces;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import resourceManager.UnitImage;

public class Building extends Unit {

	private static final Color mask = new Color(0, 0, 0, 0.7f);
	
	public Building(Coordinate location, Player owner) {
		super(location, owner);
		BASE_MOVE_RANGE = 0;
		BASE_SIGHT_RANGE = 1;
		BASE_ATTACK_RANGE = 0;
		BASE_ATTACK = 0;
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
		if(visible){
			current.draw(X+currentX, Y+currentY);
		}else{
			current.getCurrentFrame().draw(X+currentX, Y+currentY, mask);
		}
	}
}
