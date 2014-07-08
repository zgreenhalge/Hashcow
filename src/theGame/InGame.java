package theGame;

import interfaceElements.Button;
import interfaceElements.TextButton;
import interfaceElements.Menu;
import interfaceElements.buttonActions.UnImplementedAction;
import gamePieces.MapInfo;
import gamePieces.Unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.FontManager;
import utils.Logger;

public class InGame extends HCGameState {

	private int ID;
	private static int lastId = 1;
	
	private boolean playing = false;
	private MapInfo map;
	private int players;
	private int curPlayer;
	private int selectedX;
	private int selectedY;
	private Menu selected;
	
	private Button endTurnButton;

	//variables for moving map with mouse
	private boolean mouseWasDown;
	private int X;
	private int Y;
	private int centerX;
	private int centerY;
	private int startX;
	private int startY;
	private int prevX;
	private int prevY;
	private float scale;
	private Input input;
	
	public InGame(MapInfo board, int players){
		ID = ++lastId;
		map = board;
		this.players = players;
		curPlayer = 0;
		selectedX = selectedY = -1;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException{
		super.init(container, game);
		mouseWasDown = false;
		//TODO change this so that we render the center of the map on the center of the screen - will keep 
		X = centerX = (container.getWidth() - map.getWidth()*32)/2;
		Y = centerY = (container.getHeight() - map.getHeight()*32)/2;
		scale = 1.0f;
		try{
			endTurnButton = new TextButton(container, FontManager.BUTTON_FONT, "Button", 0, 0, game, 2, new UnImplementedAction());
		}catch(Exception e){Logger.loudLog(e);}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.scale((float)scale, (float)scale); 	//scale block to allow zooming
		map.render(g, X, Y);
		g.resetTransform();		//end scale block
		if(selected != null)
			selected.render(container, g);
		super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		map.update(delta);
		input = container.getInput();
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			if(mouseWasDown){
				X = prevX + input.getMouseX() - startX;
				Y = prevY + input.getMouseY() - startY;
			}else{
				startX = input.getMouseX();
				startY = input.getMouseY();
				prevX = X;
				prevY = Y;
				mouseWasDown = true;
			}
		}else{
			mouseWasDown = false;
		}
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			double mouseX = input.getMouseX();
			double mouseY = input.getMouseY();
			//map.select(-1, -1);
			if(mouseX > X*scale && mouseX < (X+map.getWidth()*32)*scale && mouseY > Y*scale && mouseY < (Y+map.getHeight()*32)*scale){
				mouseX = (int)(mouseX/scale-X)/32;
				mouseY = (int)(mouseY/scale-Y)/32;
				if(mouseX != selectedX || mouseY != selectedY){
					selectedX = (int)mouseX;
					selectedY = (int)mouseY;
					map.select(selectedX, selectedY); //handle animations
					selected = new Menu(15, 40);
					if(map.isBuilt(selectedX, selectedY)){
						//for(Ability a: map.getBuilding(selectedX, selectedY).getAbilities())
						//	selected.addButton(a.getButton());
					}
					if(map.isOccupied(selectedX, selectedY)){
						//for(Ability a: map.getUnit(selectedX, selectedY).getAbilities())
						//	selected.addButton(a.getButton());
					}
					selected.addButton(endTurnButton);
				}
			}
		}
		
		super.update(container, game, delta);

	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		
	}
	
	@Override
	public void keyPressed(int i, char c){
		if(i == Input.KEY_ESCAPE)
			Main.exit();
		if(i == Input.KEY_TAB)
			map.cycleDisplayMode();
		if(i == Input.KEY_SPACE){
			scale = 1.0f;
			X = centerX;
			Y = centerY;
		}
	}
	
	@Override
	public void mouseWheelMoved(int change){
		if(change > 0 && scale < 1.5f){
			scale *= 1.1;
			X *= .9;
			Y *= .9;
		}
		if(change < 0 && scale > 0.75f){
			scale *= 0.9;
			X *= 1.125;
			Y *= 1.125;
		}
	}
	
	public void endTurn(){
		curPlayer++;
		map.hideAll();
	}
	
	@Override
	public int getID() {
		return ID;
	}

}
