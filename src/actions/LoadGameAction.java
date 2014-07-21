package actions;

import org.newdawn.slick.state.StateBasedGame;

import theGame.GameState;
import theGame.Main;
import utils.SaveState;

public class LoadGameAction implements Action {

	private SaveState save;
	private GameState game;
	
	public LoadGameAction(){}

	public LoadGameAction(GameState game){
		this.game = game;
	}
	
	public LoadGameAction(SaveState save){
		this.save = save;
	}
	
	public void setSaveState(SaveState save){
		this.save = save;
	}
	
	public void setGameState(GameState game){
		this.game = game;
	}
	
	@Override
	public void activate() {
		if(save == null && game == null)
			return;
		if(save != null)
			game = new GameState(save);
		StateBasedGame sbg = Main.getGame();
		sbg.addState(game);
		sbg.enterState(game.getID());

	}
	
}
