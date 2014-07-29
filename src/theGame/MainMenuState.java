package theGame;


import guiElements.LayeredGUI;
import guiElements.VerticalMenu;
import guiElements.TextButton;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import actions.ExitGameAction;
import actions.GenericAction;
import actions.StateTransitionAction;
import actions.UnImplementedAction;
import actions.WrapperAction;
import resourceManager.FontManager;
import resourceManager.SoundManager;
import utils.Logger;
import utils.Settings;

public class MainMenuState extends HCGameState {

	public static final int ID = 0;
	private SoundManager sm;
	private int displayLevel;
	private TextButton NEW_BUTTON;
	private TextButton LOAD_BUTTON;
	private TextButton SETTINGS_BUTTON;
	private TextButton EXIT_BUTTON;
	private TextButton LOCAL_BUTTON;
	private TextButton NETWORK_BUTTON;
	private TextButton BACK_BUTTON;
	private TextButton HOST_BUTTON;
	private TextButton JOIN_BUTTON;
	private VerticalMenu mainMenu;
	private VerticalMenu newGameMenu;
	private VerticalMenu networkMenu;
	private LayeredGUI gui;
	
	private static final int MAIN = 1;
	private static final int NEW = 2;
	private static final int NETWORK = 3;
		
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		Font buttonFont = FontManager.BUTTON_FONT;
		BACK_BUTTON = new TextButton(container, buttonFont, "Back",
				0, 0,
				game, this.getID(), 
				new GenericAction(){
					public void activate(){
						setDisplayLevel(--displayLevel);
					}
		});
		NEW_BUTTON = new TextButton(container, buttonFont, "New Game",
				0, 0,
				game, this.getID(), 
				new GenericAction(){
					public void activate(){
						setDisplayLevel(NEW);
					}
		});
		LOAD_BUTTON = new TextButton(container, buttonFont, "Load Game",
				0, 0,
				game, this.getID(), 
				new StateTransitionAction(game, 1));
		SETTINGS_BUTTON = new TextButton(container, buttonFont, "Settings",
				0, 0,
				game, this.getID(),
				new UnImplementedAction());
		EXIT_BUTTON = new TextButton(container, buttonFont, "Exit",
				0, 0,
				game, this.getID(), 
				new ExitGameAction());
		LOCAL_BUTTON = new TextButton(container, buttonFont, "Local", 0, 0, game, this.getID(),
				new WrapperAction(new StateTransitionAction(game, GameLobbyState.ID)){
					public void activate(){
						GameLobbyState.setLocal();
						this.action.activate();
					}
		});
		NETWORK_BUTTON = new TextButton(container, buttonFont, "Network",
				0, 0,
				game, this.getID(), 
				new GenericAction(){
					public void activate(){
						setDisplayLevel(NETWORK);
					}
		});
		JOIN_BUTTON = new TextButton(container, buttonFont, "Join", 0, 0, game, this.getID(),
				new UnImplementedAction());
		HOST_BUTTON = new TextButton(container, buttonFont, "Host", 0, 0, game, this.getID(),
				new UnImplementedAction());
		JOIN_BUTTON.setEnabled(false);
		HOST_BUTTON.setEnabled(false);
		LOAD_BUTTON.setEnabled(false);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		gui.render(container, game, g);
	    super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			if(--displayLevel == 0)
				container.exit();
			setDisplayLevel(displayLevel);
		}
		super.update(container, game, delta);
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
	public void setDisplayLevel(int i){
		displayLevel = i;
		gui.hideAllBut(displayLevel);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		super.enter(container, game);
		container.setAlwaysRender(true);
		if(sm == null)
			sm = SoundManager.getManager();
		SoundManager.setVolume((Float)Settings.getSetting(Settings.VOLUME));
		sm.loopSound(SoundManager.MAIN_MENU);
		if(mainMenu == null){
			mainMenu = new VerticalMenu(0, 0);
			newGameMenu = new VerticalMenu(0, 0);
			networkMenu = new VerticalMenu(0, 0);
			mainMenu.center(true);
			newGameMenu.center(true);
			networkMenu.center(true);
			newGameMenu.setReporting(true);
			mainMenu.setReporting(true);
			mainMenu.addButton(NEW_BUTTON);
			mainMenu.addButton(LOAD_BUTTON);
			mainMenu.addButton(SETTINGS_BUTTON);
			mainMenu.addButton(EXIT_BUTTON);
			newGameMenu.addButton(LOCAL_BUTTON);
			newGameMenu.addButton(NETWORK_BUTTON);
			newGameMenu.addButton(BACK_BUTTON);
			networkMenu.addButton(JOIN_BUTTON);
			networkMenu.addButton(HOST_BUTTON);
			networkMenu.addButton(BACK_BUTTON);
		}
		if(gui == null){
			gui = new LayeredGUI();
			gui.add(mainMenu, MAIN);
			gui.add(newGameMenu, NEW);
			gui.add(networkMenu, NETWORK);
			setDisplayLevel(MAIN);
		}
	}

}
