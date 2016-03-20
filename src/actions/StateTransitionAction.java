package actions;

import org.newdawn.slick.state.StateBasedGame;

public class StateTransitionAction implements Action {

	private StateBasedGame sbg;
	private int toState;
	
	
	public StateTransitionAction(StateBasedGame game, int stateID){
		sbg = game;
		toState = stateID;
	}
	
	public void activate(){
		sbg.enterState(toState);
	}
}
