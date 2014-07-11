package theGame;

import java.util.ArrayList;

import interfaceElements.Button;
import interfaceElements.HorizontalMenu;
import interfaceElements.TextButton;
import interfaceElements.Menu;
import interfaceElements.buttonActions.ButtonAction;
import gamePieces.Building;
import gamePieces.Coordinate;
import gamePieces.MapInfo;
import gamePieces.Player;
import gamePieces.TestUnit;
import gamePieces.Unit;

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
	private ArrayList<Player> players;
	private int turnCount = 1;
	
	private MapInfo map;
	private Player curPlayer;
	private int selectedX;
	private int selectedY;
	private Menu selected;
	private HorizontalMenu menuBar;
	
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
	
	public InGame(MapInfo board, ArrayList<Player> players){
		ID = ++lastId;
		map = board;
		this.players = players;
		curPlayer = players.get(0);
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
		input = container.getInput();
		try{
			endTurnButton = new TextButton(container, FontManager.BUTTON_FONT, "End Turn", 0, 0, game, 2, new ButtonAction(){public void activate(){endTurn();}});
		}catch(Exception e){Logger.loudLog(e);}
		selected = new Menu(15, 40);
		menuBar = new HorizontalMenu(container.getWidth() - endTurnButton.getWidth(), 0);
		menuBar.addButton(endTurnButton);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		if(!playing){
			Coordinate start;
			for(int n=0; n<players.size(); n++){
				start = map.getStartingPosition(n);
				map.addUnit(new TestUnit(start, players.get(n)));
			}
			map.addUnit(new TestUnit(new Coordinate(0, 1), curPlayer));
			map.getUnit(0, 0).setCurrentHealth(4);
			map.getUnit(3, 3).setCurrentHealth(1);
			playing = true;
		}
		startTurn();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.scale((float)scale, (float)scale); 	//scale map render to zoom level
		map.render(g, X, Y);
		g.resetTransform();						//reset scale to draw HUD
		selected.render(container, g);
		menuBar.render(container, g);
		super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		map.update(delta);
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
			selected.clear();
			double mouseX = input.getMouseX();
			double mouseY = input.getMouseY();
			mouseX = (int)(mouseX/scale-X)/32;
			mouseY = (int)(mouseY/scale-Y)/32;
			if(mouseX != selectedX || mouseY != selectedY){
				selectedX = (int)mouseX;
				selectedY = (int)mouseY;
				if(selectedX >= 0 && selectedX < map.getWidth() && selectedY >= 0 && selectedY < map.getHeight()){
					for(Button b: map.select(selectedX, selectedY, curPlayer))
						selected.addButton(b);
					selected.init();
				}else{
					map.select(selectedX, selectedY, curPlayer);
				}
			}	
		}
		super.update(container, game, delta);
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
	
	public void startTurn(){
		curPlayer.age();
		for(Unit u: curPlayer.owned())
			u.setVisible(true);
		map.setSightMap(curPlayer.getSightMap());
		map.applySightMap();
	}
	
	public void endTurn(){
		curPlayer = players.get(curPlayer.getId() % players.size()); //get the next player
		map.hideAll();
		if(playing)
			startTurn();
		turnCount++;
	}
	
	@Override
	public int getID() {
		return ID;
	}

}
