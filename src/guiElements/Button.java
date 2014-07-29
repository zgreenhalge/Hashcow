package guiElements;

import actions.Action;

public interface Button extends Component{
	
	public Action getAction();
	public void setAction(Action a);
	public abstract boolean isEnabled();
	public abstract void setEnabled(boolean b);
}
