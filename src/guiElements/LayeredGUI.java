package guiElements;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import theGame.Main;
import utils.Logger;
import utils.OneToOneMap;
import utils.Settings;

public class LayeredGUI implements GUI{
	
	private static final int MAX_LAYER = 10;
	
	private OneToOneMap<Integer, ArrayList<Component>> tiers;
	private OneToOneMap<Component, Integer> lookup;
	private int currentLayer = 4;
	
	public LayeredGUI(){
		tiers = new OneToOneMap<Integer, ArrayList<Component>>();
		lookup = new OneToOneMap<Component, Integer>();	
		Main.getStaticContainer().getInput().addListener(this);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g){
		for(int layer = MAX_LAYER-1; layer > -1; layer--){
			if(tiers.containsKey(layer))
				for(Component c: tiers.getValue(layer))
					c.render(container, game, g);
		}
	}
	
	
	public void update(GameContainer container, StateBasedGame game, int delta){
		for(Component c: lookup.keySet())
			c.update(container, game, delta);
	}
	
	public void add(Component c){
		lookup.add(c, currentLayer);
		if(!tiers.containsKey(currentLayer))
			tiers.add(currentLayer, new ArrayList<Component>());
		tiers.getValue(currentLayer).add(c);
	}
	
	public void setLayer(int i){
		if(i >= MAX_LAYER)
			i = MAX_LAYER-1;
		else if(i < 0)
			i = 0;
		currentLayer = i;
	}
	
	public void add(Component c, int layer){
		if(layer >= MAX_LAYER)
			layer = MAX_LAYER-1;
		else if(layer < 0)
			layer = 0;
		lookup.add(c, layer);
		if(!tiers.containsKey(layer))
			tiers.add(layer, new ArrayList<Component>());
		tiers.getValue(layer).add(c);
	}
	
	public void remove(Component c){
		if(lookup.containsKey(c)){
			tiers.getValue(lookup.getValue(c)).remove(c);
			lookup.remove(c);
		}
	}
	
	public void hideLayer(int layer){
		if(tiers.containsKey(layer))
			for(Component r: tiers.getValue(layer))
				r.setHidden(true);
	}
	
	public void showLayer(int layer){
		if(tiers.containsKey(layer))
			for(Component r: tiers.getValue(layer))
				r.setHidden(false);
	}
	
	public void hideAllBut(int layer){
		for(int l = 0; l<MAX_LAYER; l++)
			if(tiers.containsKey(l))
				for(Component c: tiers.getValue(l))
					c.setHidden(true);
		if(tiers.containsKey(layer))
			for(Component c: tiers.getValue(layer))
				c.setHidden(false);
	}

	public void showAllBut(int layer){
		for(int l = 0; l<MAX_LAYER; l++)
			if(tiers.containsKey(l))
				for(Component c: tiers.getValue(l))
					c.setHidden(false);
		if(tiers.containsKey(layer))
			for(Component c: tiers.getValue(layer))
				c.setHidden(true);
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
//		if((Boolean) Settings.getSetting("dev"))
//			Logger.logNote("Mouse click: " + x + "," + y);
		tierLoop: //break out of this block once input is consumed
		for(int layer = 0; layer<MAX_LAYER; layer++)
			if(tiers.containsKey(layer))
				for(Component c: tiers.getValue(layer))
					if(c.mouseClick(button, x, y)) //if event is consumed, no other layers get it
						break tierLoop;
	}

	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		tierLoop:
		for(int layer = 0; layer<MAX_LAYER; layer++){
			if(tiers.containsKey(layer))
				for(Component c: tiers.getValue(layer))
					if(c.mouseMove(oldX, oldY, newX, newY)) //if event is consumed, no other layers get it
						break tierLoop;
		}
		
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(int change){
		tierLoop:
			for(int layer = 0; layer<MAX_LAYER; layer++){
				if(tiers.containsKey(layer))
					for(Component c: tiers.getValue(layer))
						if(c.mouseWheelMove(change)) //if event is consumed, no other layers get it
							break tierLoop;
			}
	}

	@Override
	public void inputEnded() {
		//does nothing in this context		
	}

	@Override
	public void inputStarted() {
		//does nothing in this context
	}

	@Override
	public boolean isAcceptingInput() {
		//does nothing in this context
		return true;
	}

	@Override
	public void setInput(Input in) {
		//does nothing in this context
	}

	@Override
	public void keyPressed(int key, char c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int key, char c) {
		//does nothing in this context
	}

	@Override
	public void controllerButtonPressed(int controller, int button) {
		//does nothing in this context
	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		//does nothing in this context		
	}

	@Override
	public void controllerDownPressed(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerDownReleased(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerLeftPressed(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerLeftReleased(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerRightPressed(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerRightReleased(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerUpPressed(int controller) {
		//does nothing in this context		
	}

	@Override
	public void controllerUpReleased(int controller) {
		//does nothing in this context
	}
	
}
