package theGame;

import java.util.ArrayList;

import gamePieces.Coordinate;
import gamePieces.MapInfo;
import gamePieces.Player;
import gamePieces.TestHQ;
import gamePieces.TestUnit;
import gamePieces.Unit;
import guiElements.Button;
import guiElements.HorizontalMenu;
import guiElements.TextButton;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import actions.Action;
import resourceManager.FontManager;
import utils.Logger;
import utils.SaveState;
import utils.Settings;

public class GameState extends HCGameState {

	private int ID;
	private static int lastId = 2;
	
	private transient boolean init;
	private boolean playing = false;
	private ArrayList<Player> players;
	private int turnCount = 1;
	private GameState gameState;
	
	private MapInfo map;
	private Player curPlayer;
	private int selectedX;
	private int selectedY;
	private HorizontalMenu menuBar;
	private Button endTurnButton;

	//variables for moving map with mouse
	private boolean mouseWasDown;
	private int X;
	private int Y;
	private int startX;
	private int startY;
	private int prevX;
	private int prevY;
	private float scale;
	private Input input;
	
	public GameState(MapInfo board, ArrayList<Player> players){
		ID = ++lastId;
		map = board;
		this.players = players;
		selectedX = selectedY = -1;
	}
	
	public GameState(SaveState save){
		ID = ++lastId;
		map = save.getMap();
		players = save.getPlayers();
		curPlayer = players.get(save.getCurPlayer());
		turnCount = save.getTurn();
		selectedX = selectedY = -1;
		playing = true;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException{
		super.init(container, game);
		gameState = this;
		mouseWasDown = false;
		//X = centerX = (container.getWidth() - map.getWidth()*32)/2;
		//Y = centerY = (container.getHeight() - map.getHeight()*32)/2;
		scale = 1.0f;
		input = container.getInput();
		try{
			endTurnButton = new TextButton(container, FontManager.BUTTON_FONT, "End Turn", 0, 0, game, ID, new Action(){public void activate(){endTurn();}});
		}catch(Exception e){Logger.loudLog(e);}
		menuBar = new HorizontalMenu(container.getWidth() - (endTurnButton.getWidth() + FontManager.BUTTON_TRUETYPE.getWidth("Save Game")), 0);
		menuBar.addButton(endTurnButton);
		try{
			menuBar.addButton(new TextButton(container, FontManager.BUTTON_FONT, "Save Game", 0, 0, game, ID, new Action(){public void activate(){SaveState.save(new SaveState(gameState));}}));
		}catch(Exception e){Logger.loudLog(e);}
		init = true;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		if(!init)
			init(container, game);
		if(!playing){
			Coordinate start;
			for(int n=0; n<players.size(); n++){
				curPlayer = players.get(n);
				curPlayer.setLastX((Main.getStaticContainer().getWidth()/2 - map.getStartingPosition(curPlayer.getId()-1).X()*32));
				curPlayer.setLastY((Main.getStaticContainer().getHeight()/2 - map.getStartingPosition(curPlayer.getId()-1).Y()*32));
				start = map.getStartingPosition(n);
				(new TestHQ(start, players.get(n), map)).register();
				(new TestUnit(start, players.get(n), map)).register();
				start = Coordinate.copy(start);
				if(start.X() > 1)
					start.X(-1);
				else
					start.X(1);
				(new TestUnit(start, players.get(n), map)).register();
			}
			curPlayer = players.get(0);
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
			double mouseX = input.getMouseX();
			double mouseY = input.getMouseY();
			mouseX = (int)(mouseX/scale-X)/32;
			mouseY = (int)(mouseY/scale-Y)/32;
			if(mouseX != selectedX || mouseY != selectedY){
				selectedX = (int)mouseX;
				selectedY = (int)mouseY;
				map.select(selectedX, selectedY, curPlayer);
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
			X = (Main.getStaticContainer().getWidth()/2 - map.getStartingPosition(curPlayer.getId()-1).X()*32);
			Y = (Main.getStaticContainer().getHeight()/2 - map.getStartingPosition(curPlayer.getId()-1).Y()*32);
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
		X = curPlayer.getLastX();
		Y = curPlayer.getLastY();
		for(Unit u: curPlayer.owned())
			u.setVisible(true);
		map.setSightMap(curPlayer.getSightMap());
		map.applySightMap();
	}
	
	public void endTurn(){
		curPlayer.setLastX(X);
		curPlayer.setLastY(Y);
		if(playing){
			curPlayer.age();
			map.hideAll();
			curPlayer = players.get(curPlayer.getId() % players.size()); //get the next player
			if(players.indexOf(curPlayer) == 0)
				turnCount++;
			if((boolean)Settings.getSetting(Settings.DEV_MODE))
				Logger.loudLogLine("Turn  " + turnCount + "." + players.indexOf(curPlayer));
			startTurn();
		}
	}
	
	public void saveGame(){
		SaveState.save(new SaveState(this));
	}
	
	@Override
	public int getID() {
		return ID;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public MapInfo getMap(){
		return map;
	}
	
	public int getTurn(){
		return turnCount;
	}
	
	public Player getCurrentPlayer(){
		return curPlayer;
	}

}
