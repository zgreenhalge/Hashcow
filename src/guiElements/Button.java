package guiElements;

import actions.Action;

/**
 * An common interface so both TextButton and ImageButton can be grouped together and addressed equally 
 *
 */
public interface Button extends Component{
	
	public Action getAction();
	public void setAction(Action a);
	public String getName();
	public Button setName(String s);
	public boolean isReporting();
	public void setReport(boolean b);
	public abstract boolean isEnabled();
	public abstract void setEnabled(boolean b);
}
