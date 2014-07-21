package theGame;


import gamePieces.MapInfo;
import gamePieces.Player;
import guiElements.Button;
import guiElements.TextButton;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import actions.ExitGameAction;
import actions.LoadGameAction;
import actions.StateTransitionAction;
import actions.UnImplementedAction;
import resourceManager.FontManager;
import resourceManager.SoundManager;
import utils.Settings;

public class MainMenuState extends HCGameState {

	public static final int ID = 0;
	private SoundManager sm;
	private TextButton NEW;
	private TextButton LOAD;
	private TextButton SETTINGS;
	private TextButton EXIT;
	private ArrayList<Button> buttons;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		Font buttonFont = FontManager.BUTTON_FONT;
		TrueTypeFont ttfont = FontManager.BUTTON_TRUETYPE;
		ArrayList<Player> temp = new ArrayList<Player>();
		temp.add(new Player(1, Color.green));
		temp.add(new Player(2, Color.red));
		NEW = new TextButton(container, buttonFont, "New Game",
				container.getWidth()/2-ttfont.getWidth("New Game")/2, container.getHeight()-ttfont.getLineHeight()*6,
				game, this.getID(), 
				new LoadGameAction(new GameState(MapInfo.TEST_MAP, temp)));
		LOAD = new TextButton(container, buttonFont, "Load Game",
				container.getWidth()/2-ttfont.getWidth("Load Game")/2, container.getHeight()-ttfont.getLineHeight()*5,
				game, this.getID(), 
				new StateTransitionAction(game, 1));
		SETTINGS = new TextButton(container, buttonFont, "Settings",
				container.getWidth()/2-ttfont.getWidth("Settings")/2, container.getHeight()-ttfont.getLineHeight()*4,
				game, this.getID(),
				new UnImplementedAction());
		EXIT = new TextButton(container, buttonFont, "Exit",
				container.getWidth()/2-ttfont.getWidth("Exit")/2, container.getHeight()-ttfont.getLineHeight()*3,
				game, this.getID(), 
				new ExitGameAction());
		buttons = new ArrayList<Button>();
		buttons.add(NEW);
		buttons.add(LOAD);
		buttons.add(SETTINGS);
		buttons.add(EXIT);
		for(Button b: buttons)
			b.setReport(true);
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setAntiAlias(true);
		if(buttons != null)
			for(Button b: buttons){
				b.render((GUIContext)container, g);
			}
	    super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			container.exit();
		if(container.getInput().isKeyPressed(Input.KEY_ENTER))
			game.enterState(2);
		super.update(container, game, delta);
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		sm = SoundManager.getManager();
		SoundManager.setVolume((Float)Settings.getSetting(Settings.VOLUME));
		sm.loopSound(SoundManager.MAIN_MENU);
		super.enter(container, game);
	}

}
