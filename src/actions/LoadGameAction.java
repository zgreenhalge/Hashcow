package actions;

import org.newdawn.slick.state.StateBasedGame;

import theGame.InGameState;
import theGame.Main;
import utils.SaveState;

public class LoadGameAction implements Action {

	private SaveState save;
	private InGameState game;
	
	public LoadGameAction(){}

	public LoadGameAction(InGameState game){
		this.game = game;
	}
	
	public LoadGameAction(SaveState save){
		this.save = save;
	}
	
	public void setSaveState(SaveState save){
		this.save = save;
	}
	
	public void setGameState(InGameState game){
		this.game = game;
	}
	
	@Override
	public void activate() {
		if(save == null && game == null)
			return;
		if(save != null)
			game = new InGameState(save);
		StateBasedGame sbg = Main.getGame();
		sbg.addState(game);
		sbg.enterState(game.getID());

	}
	
}
