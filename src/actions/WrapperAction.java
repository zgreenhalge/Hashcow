package actions;


public class WrapperAction implements Action {

	protected Action action;
	
	public WrapperAction(Action a){
		action = a;
	}
	
	@Override
	public void activate() {
		action.activate();
	}
	
	public Action getWrappedAction(){
		return action;
	}

}
