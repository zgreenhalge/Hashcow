package interfaceElements.buttonActions;

import org.newdawn.slick.SlickException;
import utils.Logger;

public class UnImplementedAction extends ButtonAction {

	public UnImplementedAction(){}
	
	@Override
	public void activate(){
		Logger.loudLog(new SlickException("Button not yet implemented"));
	}
}
