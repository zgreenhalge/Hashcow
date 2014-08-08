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
	private ArrayList<File> fileList;
	private File saveFolder;
	
	private int mapWidth;
	private int mMapX;
	private int mMapY;
	
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
		mapWidth = 3*gc.getWidth()/12;
		mMapX = 2*gc.getWidth()/3;
		mMapY = gc.getWidth()/20;
		gameList = new VerticalMenu(mMapY, mMapY);
		savedGames = new ArrayList<SaveState>();
		fileList = new ArrayList<File>();
		saveFolder = new File("saves");
		minimap = new Image(mapWidth, mapWidth);
		loadGame = new LoadGameAction();
		loadGameButton = new TextButton(gc, FontManager.BUTTON_FONT, "Load",
				mMapX, mMapY + gc.getHeight() - FontManager.BUTTON_TRUETYPE.getLineHeight()*4,
				sbg, ID,
				loadGame);
		backButton = new TextButton(gc, FontManager.BUTTON_FONT, "Back",
				mMapX + mapWidth - FontManager.BUTTON_TRUETYPE.getWidth("Back"), mMapY + gc.getHeight() - FontManager.BUTTON_TRUETYPE.getLineHeight()*4,
				sbg, ID, 
				new StateTransitionAction(sbg, MainMenuState.ID));
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		super.enter(container, game);
		if(gui == null){
			TextButton temp;
			if(saveFolder.exists())
				for(File f: saveFolder.listFiles()){
					savedGames.add(null);
					fileList.add(f);
					temp = new TextButton(container, FontManager.DEFAULT_FONT, f.getName(), 0, 0,
							game, ID, null);
					temp.setAction(new SelectTextButtonAction(temp){
						public void activate(){
							activateBorders();
							if(selectedButton != null)
								selectedButton.setBorderEnabled(false);
							selectedButton = this.getButton();
							save = savedGames.get(gameList.getIndex(selectedButton));
							if(save == null)
								save = SaveState.load(fileList.get(savedGames.indexOf(save)).getAbsolutePath());
							save.getMap().load();
							minimap = save.getMap().getMinimap(mapWidth, mapWidth);
							loadGame.setSaveState(save);
						}
					});
					gameList.addButton(temp);
				}
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
		minimap.draw(mMapX, mMapY);
		if(save != null){
			int day = (save.getTurn()*save.getTurnLength())/24;
			int time = (save.getTurn()*save.getTurnLength())%24;
			g.drawString("DAY " + day + " " + time + ":00", mMapX, mMapY + mapWidth + g.getFont().getLineHeight());
			g.drawString("Players: " + save.getPlayers().size(), mMapX, mMapY + mapWidth + g.getFont().getLineHeight()*2);
		}
		super.render(container, game, g);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			game.enterState(MainMenuState.ID);
		super.update(container, game, delta);
		gui.update(container, game, delta);
	}
	
	@Override
	public int getID(){
		return ID;
	}
	
}
