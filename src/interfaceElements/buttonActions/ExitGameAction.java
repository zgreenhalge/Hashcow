package interfaceElements.buttonActions;

import theGame.Main;

public class ExitGameAction extends ButtonAction {
	
	public ExitGameAction(){}
	
	@Override
	public void activate(){
		Main.exit();
	}
	
}
