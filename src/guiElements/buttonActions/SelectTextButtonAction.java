package guiElements.buttonActions;

import org.newdawn.slick.Color;

import guiElements.TextButton;

public class SelectTextButtonAction extends ButtonAction {

	private TextButton button;
	
	public SelectTextButtonAction(TextButton b){
		button = b;
	}
	
	public void nativeActivate(){
		button.setColor(Color.yellow.darker());
		button.setBorderEnabled(true);		
	}
	
	public void activate(){
		nativeActivate();
	}
	
	public TextButton getButton(){
		return button;
	}
}
