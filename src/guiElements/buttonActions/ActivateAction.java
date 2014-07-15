package guiElements.buttonActions;

import gamePieces.Abilities.Ability;

public class ActivateAction extends ButtonAction {

	private Ability ability;
	
	public ActivateAction(Ability a){
		ability = a;
	}
	
	public void activate(){
		ability.activate();
	}
}
