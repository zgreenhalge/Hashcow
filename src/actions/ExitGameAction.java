package actions;

import theGame.Main;

public class ExitGameAction implements Action {
	
	@Override
	public void activate(){
		Main.exit();
	}
	
}
