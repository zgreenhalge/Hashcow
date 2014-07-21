package actions;

import org.newdawn.slick.GameContainer;

public class ToggleFPSAction implements Action {

	private GameContainer container;
	
	public ToggleFPSAction(GameContainer gc){
		container = gc;
	}
	
	@Override
	public void activate(){
		container.setShowFPS(!container.isShowingFPS());
	}

}
