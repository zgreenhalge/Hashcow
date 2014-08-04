package theGame;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import actions.GenericAction;
import actions.LoadGameAction;
import actions.SelectTextButtonAction;
import actions.StateTransitionAction;
import resourceManager.FontManager;
import utils.SaveState;
import gamePieces.Unit;
import guiElements.LayeredGUI;
import guiElements.VerticalMenu;
import guiElements.TextButton;

public class LoadGameState extends HCGameState {

	private static final int ID = 1;
	
	private VerticalMenu gameList;
	private ArrayList<SaveState> savedGames;
	private File saveFolder;
	private int mapWidth;
	
	private LayeredGUI gui;
	private TextButton selectedButton;
	private TextButton loadGameButton;
	private TextButton backButton;
	private SaveState save;
	private Image minimap;
	private LoadGameAction loadGame;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		super.init(gc, sbg);
		gameList = new VerticalMenu(20, 20);
		savedGames = new ArrayList<SaveState>();
		TextButton temp;
		saveFolder = new File("saves");
		mapWidth = gc.getWidth()/5;
		minimap = new Image(mapWidth, mapWidth);
		loadGame = new LoadGameAction();
		if(saveFolder.exists())
			for(File f: saveFolder.listFiles()){
				savedGames.add(SaveState.load(f.getPath()));
				temp = new TextButton(gc, FontManager.DEFAULT_FONT, f.getName(), 0, 0,
						sbg, ID, null);
				temp.setAction(new SelectTextButtonAction(temp){
					public void activate(){
						activateBorders();
						if(selectedButton != null)
							selectedButton.setBorderEnabled(false);
						selectedButton = this.getButton();
						save = savedGames.get(gameList.getIndex(selectedButton));
						for(Unit u: save.getMap().getAll())
							u.load();
						minimap = save.getMap().getMinimap(mapWidth, mapWidth);
						loadGame.setSaveState(save);
					}
				});
				gameList.addButton(temp);
			}
		loadGameButton = new TextButton(gc, FontManager.BUTTON_FONT, "Load",
				600, 370,
				sbg, ID,
				loadGame);
		backButton = new TextButton(gc, FontManager.BUTTON_FONT, "Back",
				0, 0,
				sbg, ID, 
				new StateTransitionAction(sbg, MainMenuState.ID));
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		super.enter(container, game);
		if(gui == null){
			loadGameButton.setBorderEnabled(true);
			backButton.setBorderEnabled(true);
			loadGameButton.setReport(true);
			backButton.setReport(true);
			gui = new LayeredGUI();
			gui.add(gameList);
			gui.add(loadGameButton);
			gui.add(backButton);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		gui.render(container, game, g);
		minimap.draw(600, 120);
		if(save != null){
			int day = (save.getTurn()*save.getTurnLength())/24;
			int time = (save.getTurn()*save.getTurnLength())%24;
			g.drawString("DAY " + day + " " + time + ":00", 600, 330);
			g.drawString("Players: " + save.getPlayers().size(), 600, 350);
		}
		super.render(container, game, g);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			game.enterState(MainMenuState.ID);
		super.update(container, game, delta);
	}
	
	@Override
	public int getID(){
		return ID;
	}
	
}
