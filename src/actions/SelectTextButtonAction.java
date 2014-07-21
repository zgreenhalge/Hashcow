package actions;

import org.newdawn.slick.Color;

import guiElements.TextButton;

public class SelectTextButtonAction implements Action {

	private TextButton button;
	
	public SelectTextButtonAction(TextButton b){
		button = b;
	}
	
	public void activateBorders(){
		button.setColor(Color.yellow.darker());
		button.setBorderEnabled(true);		
	}
	
	public void activate(){
		activateBorders();
	}
	
	public TextButton getButton(){
		return button;
	}
}
