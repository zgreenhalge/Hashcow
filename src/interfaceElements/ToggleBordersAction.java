package interfaceElements;

import java.util.ArrayList;

public class ToggleBordersAction extends ButtonAction {

	private ArrayList<Button> buttons;
	
	public ToggleBordersAction(ArrayList<Button> b){
		buttons = b;
	}
	
	public void setButtonList(ArrayList<Button> b){
		buttons = b;
	}
	
	@Override
	public void activate() {
		for(Button b: buttons){
			if(b instanceof TextButton){
				TextButton tb = (TextButton)b;
				tb.setBorderEnabled(!tb.isBorderEnabled());
			}
		}
	}

}
