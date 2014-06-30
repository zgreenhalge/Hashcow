package theGame;

import gamePieces.MapInfo;
import gamePieces.Unit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import utils.Logger;

public class InGame extends HCGameState {

	private int ID;
	private static int lastId = 1;
	
	private boolean playing = false;
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
	private float scale;
	private Input input;
	
	public InGame(MapInfo board, int players){
		ID = ++lastId;
		map = board;
		this.players = players;
		curPlayer = 0;
		
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException{
		mouseWasDown = false;
		//TODO change this so that we render the center of the map on the center of the screen - will keep 
		X = (container.getWidth() - map.getWidth())/2;
		Y = (container.getHeight() - map.getHeight())/2;
		scale = 1.0f;
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.scale(scale, scale); 	//scale block to allow zooming
		map.render(g, X, Y);
		g.resetTransform();		//end scale block
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
	}

	@Override
	public void mouseWheelMoved(int change){
		if(change > 0 && scale < 1.5f)
			scale = scale*1.1f;
		if(change < 0 && scale > 0.5f)
			scale = scale*.9f;
	}
	
	@Override
	public int getID() {
		return ID;
	}

}
