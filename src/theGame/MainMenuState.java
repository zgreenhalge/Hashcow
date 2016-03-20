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
import resourceManager.ImageManager;
import resourceManager.SoundManager;
import utils.Settings;

public class MainMenuState extends HCGameState {

	public static final int ID = 0;
	private SoundManager sm;
	private MENU_STATE displayLevel;
	private TextButton NEW_BUTTON;
	private TextButton LOAD_BUTTON;
//	private TextButton SETTINGS_BUTTON;
	private TextButton EXIT_BUTTON;
	private TextButton LOCAL_BUTTON;
	private TextButton BACK_BUTTON;
//	private TextButton NETWORK_BUTTON;
//	private TextButton HOST_BUTTON;
//	private TextButton JOIN_BUTTON;
	private VerticalMenu mainMenu;
	private VerticalMenu newGameMenu;
//	private VerticalMenu networkMenu;
	private LayeredGUI gui;
	
	public static enum MENU_STATE{
		MAIN(0),
		NEW(1),
		NETWORK(2);
		
		private static final int MAX = 2;
		private int index;
		
		private MENU_STATE(int i){
			index = i;
		}
		
		public MENU_STATE getState(int i)
		{
			switch ( i )
			{
				case 0:
					return MAIN;
				case 1:
					return NEW;
				case 3:
					return NETWORK;
				default:
					return null;
			}
		}
		
		public MENU_STATE getNextState()
		{
			if(index == MAX)
			{
				return null;
			} 
			else 
			{
				return getState(index+1);
			}
		}
		
		public MENU_STATE getPreviousState()
		{	
			if(index == 0)
			{
				return null;
			} 
			else 
			{
				return getState(index-1);
			}
		}
		
		public int getIndex()
		{
			return index;
		}
		
	}
		
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		Font buttonFont = FontManager.BUTTON_FONT;
		BACK_BUTTON = new TextButton(
				container,
				buttonFont,
				"Back",
				0, 0,
				game,
				this.getID(), 
				new GenericAction(){
					public void activate(){
						setDisplayLevel(displayLevel = displayLevel.getPreviousState());
					}
		});
		NEW_BUTTON = new TextButton(
				container, 
				buttonFont, 
				"New Game",
				0, 0,
				game, 
				this.getID(), 
				new GenericAction(){
					public void activate(){
						setDisplayLevel(MENU_STATE.NEW);
					}
		});
		LOAD_BUTTON = new TextButton(
				container, 
				buttonFont, 
				"Load Game",
				0, 0,
				game, 
				this.getID(), 
				new WrapperAction(new StateTransitionAction(game, 1))
				{
					public void activate()
					{
						if(Main.LOAD_STATE == null)
						{
							try {
								Main.addLoadState();
							} catch (SlickException e) {
								e.printStackTrace();
								Main.exit();
							}
						}
						this.action.activate();
					}
				});
//		SETTINGS_BUTTON = new TextButton(container, buttonFont, "Settings",
//				0, 0,
//				game, this.getID(),
//				new UnImplementedAction());
		EXIT_BUTTON = new TextButton(
				container, 
				buttonFont, 
				"Exit",
				0, 0,
				game, 
				this.getID(), 
				new ExitGameAction());
		LOCAL_BUTTON = new TextButton(
				container, 
				buttonFont, 
				"Local", 
				0, 0, 
				game, 
				this.getID(),
				new WrapperAction(new StateTransitionAction(game, GameLobbyState.ID))
				{
					public void activate()
					{
						if(Main.LOBBY_STATE == null)
						{
							try {
								Main.addLobbyState();
							} catch (SlickException e) {
								e.printStackTrace();
								Main.exit();
							}
						}
						GameLobbyState.setLocal();
						this.action.activate();
					}
				}
		);
//		NETWORK_BUTTON = new TextButton(container, buttonFont, "Network",
//				0, 0,
//				game, this.getID(), 
//				new GenericAction(){
//					public void activate(){
//						setDisplayLevel(NETWORK);
//					}
//		});
//		JOIN_BUTTON = new TextButton(container, buttonFont, "Join", 0, 0, game, this.getID(),
//				new UnImplementedAction());
//		HOST_BUTTON = new TextButton(container, buttonFont, "Host", 0, 0, game, this.getID(),
//				new UnImplementedAction());
//		NETWORK_BUTTON.setEnabled(false);
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
			if((displayLevel = displayLevel.getPreviousState()) == null)
				container.exit();
			setDisplayLevel(displayLevel);
		}
		super.update(container, game, delta);
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
	public void setDisplayLevel(MENU_STATE state){
		displayLevel = state;
		gui.hideAllBut(displayLevel.getIndex());
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		super.enter(container, game);
		container.setAlwaysRender(true);
		if(sm == null)
			sm = SoundManager.getManager();
//		SoundManager.setVolume((Float)Settings.getSetting(Settings.VOLUME));
		SoundManager.setVolume((float) 0.05);
		sm.loopSound(SoundManager.MAIN_MENU);
		if(mainMenu == null){
			mainMenu = new VerticalMenu(0, 0);
			newGameMenu = new VerticalMenu(0, 0);
//			networkMenu = new VerticalMenu(0, 0);
			mainMenu.center(true);
			newGameMenu.center(true);
//			networkMenu.center(true);
			newGameMenu.setReporting(true);
			mainMenu.setReporting(true);
			mainMenu.addButton(NEW_BUTTON);
			mainMenu.addButton(LOAD_BUTTON);
//			mainMenu.addButton(SETTINGS_BUTTON);
			mainMenu.addButton(EXIT_BUTTON);
			newGameMenu.addButton(LOCAL_BUTTON);
			newGameMenu.addButton(BACK_BUTTON);
//			newGameMenu.addButton(NETWORK_BUTTON);
//			networkMenu.addButton(JOIN_BUTTON);
//			networkMenu.addButton(HOST_BUTTON);
//			networkMenu.addButton(BACK_BUTTON);
		}
		if(gui == null){
			gui = new LayeredGUI();
			gui.add(mainMenu, MENU_STATE.MAIN.getIndex());
			gui.add(newGameMenu, MENU_STATE.NEW.getIndex());
//			gui.add(networkMenu, NETWORK);
			setDisplayLevel(MENU_STATE.MAIN);
		}
	}

}
