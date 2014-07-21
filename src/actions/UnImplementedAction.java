package actions;

import org.newdawn.slick.SlickException;
import utils.Logger;

public class UnImplementedAction implements Action {

	public UnImplementedAction(){}
	
	@Override
	public void activate(){
		Logger.loudLog(new SlickException("Button not yet implemented"));
	}
}
