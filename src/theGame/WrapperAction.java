package theGame;

import actions.Action;

public class WrapperAction implements Action {

	protected Action action;
	
	public WrapperAction(Action a){
		action = a;
	}
	
	@Override
	public void activate() {
		action.activate();
	}

}
