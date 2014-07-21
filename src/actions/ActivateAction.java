package actions;

import gamePieces.Abilities.Ability;

public class ActivateAction implements Action {

	private Ability ability;
	
	public ActivateAction(Ability a){
		ability = a;
	}
	
	public void activate(){
		ability.activate();
	}
}
