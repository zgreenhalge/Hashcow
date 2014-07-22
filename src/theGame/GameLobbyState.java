package theGame;

import gamePieces.MapInfo;
import gamePieces.Player;
import guiElements.ExpandingMenu;
import guiElements.TextButton;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.FontManager;
import actions.GenericAction;
import actions.StateTransitionAction;
import actions.UnImplementedAction;

public class GameLobbyState extends HCGameState {

	public static final int ID = 2;

	private ArrayList<Player> players;
	private ArrayList<MapInfo> maps;
	private ExpandingMenu menu;
	private TextButton dropDownButton;
	private TextButton startButton;
	private TextButton backButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{
		players = new ArrayList<Player>();
		maps = new ArrayList<MapInfo>();
		Font buttonFont = FontManager.BUTTON_FONT;
		dropDownButton = new TextButton(container, buttonFont, "Press me...",
				0, 0,
				game, this.getID(),
				new GenericAction());
		backButton = new TextButton(container, buttonFont, "Back",
				0, 0,
				game, this.getID(), 
				new StateTransitionAction(game, MainMenuState.ID));
		startButton = new TextButton(container, buttonFont, "Start",
				0, 0,
				game, this.getID(),
				new UnImplementedAction());
		menu = new ExpandingMenu(container, dropDownButton, container.getWidth()/2, container.getHeight()/2, 2, 20);
		menu.addButton(backButton);
		menu.addButton(startButton);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		menu.render(container, g);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		
	}
	
	@Override
	public int getID(){
		return ID;
	}


	public static void setLocal(){
		//set to local play
	}
	
	public static void setNetwork(){
		//set to network play
	}
	
	
}
