package interfaceElements.buttonActions;

import org.newdawn.slick.GameContainer;

public class ToggleFPSAction extends ButtonAction {

	private GameContainer container;
	
	public ToggleFPSAction(GameContainer gc){
		container = gc;
	}
	
	@Override
	public void activate(){
		container.setShowFPS(!container.isShowingFPS());
	}

}
