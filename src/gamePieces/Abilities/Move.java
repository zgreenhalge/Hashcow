package gamePieces.Abilities;


import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import actions.ActivateAction;
import actions.Action;
import resourceManager.ImageManager;
import theGame.Main;
import utils.Logger;
import gamePieces.Coordinate;
import gamePieces.Player;
import gamePieces.Selectable;
import gamePieces.Unit;
import guiElements.Button;
import guiElements.ImageButton;

public class Move implements Ability, Selectable{

	private Unit unit;
	private ImageButton button;
	
	public Move(Unit moving){
		unit = moving;
		Action activate = new ActivateAction(this);
		SpriteSheet ss = ImageManager.getSpriteSheet("", 32, 32, 1);
		try {
			button = new ImageButton(Main.getStaticContainer(), ss, 0, 0, Main.getGame(), Main.getGame().getCurrentStateID(), activate);
		} catch (SlickException e) {
			Logger.log(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public Button getButton() {
		return button;
	}

	@Override
	public void activate() {
		unit.toggleDisplayMove();
		unit.getMap().requestNextInput(this);
	}

	@Override
	public boolean isSelected() {
		//unused
		return false;
	}

	@Override
	public void deselect(){
		//unused
	}

	@Override
	public void select(Coordinate selected, Player selector){
		unit.toggleDisplayMove();
		if(unit.getMovementRange().contains(selected))
			unit.moveTo(selected);
	}

}
