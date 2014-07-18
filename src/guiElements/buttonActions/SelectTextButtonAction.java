package guiElements.buttonActions;

import org.newdawn.slick.Color;

import guiElements.TextButton;

public class SelectTextButtonAction extends ButtonAction {

	private TextButton button;
	
	public SelectTextButtonAction(TextButton b){
		button = b;
	}
	
	public void activate(){
		button.setColor(Color.yellow.darker());
		button.setBorderEnabled(true);
	}
	
	public TextButton getButton(){
		return button;
	}
}
