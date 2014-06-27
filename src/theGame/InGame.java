package theGame;

import gamePieces.MapInfo;
import gamePieces.Unit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class InGame extends HCGameState {

	private int ID;
	private static int lastId = 1;
	
	private boolean playing = true;
	private MapInfo map;
	private Unit[][] units;
	private int players;
	private int curPlayer;

	//variables for moving map with mouse
	private boolean mouseWasDown;
	private int X;
	private int Y;
	private int startX;
	private int startY;
	private int prevX;
	private int prevY;
	private Input input;
	
	public InGame(MapInfo board){
		ID = ++lastId;
		map = board;
		players = map.getNumPlayers();
		curPlayer = 0;
		
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException{
		mouseWasDown = false;
		//TODO change this so that we render the center of the map on the center of the screen - will keep 
		X = (container.getWidth() - map.getWidth())/2;
		Y = (container.getHeight() - map.getHeight())/2;
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		map.render(X, Y);
		super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		map.update(delta);
		input = container.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			game.enterState(MainMenu.ID);
		}
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
		
		super.update(container, game, delta);

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
