package theGame;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.FontManager;
import utils.SaveStruct;
import guiElements.Menu;
import guiElements.TextButton;
import guiElements.buttonActions.ButtonAction;
import guiElements.buttonActions.SelectTextButtonAction;
import guiElements.buttonActions.UnImplementedAction;

public class LoadGameState extends HCGameState {

	private static final int ID = 1;
	
	private Menu gameList;
	private ArrayList<SaveStruct> savedGames;
	private File saveFolder;
	
	private TextButton selectedButton;
	private TextButton loadGameButton;
	private SaveStruct save;
	private Image minimap;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		gameList = new Menu(20, 20);
		savedGames = new ArrayList<SaveStruct>();
		TextButton temp;
		saveFolder = new File("saves");
		minimap = new Image(200, 200);
		if(saveFolder.exists())
			for(File f: saveFolder.listFiles()){
				savedGames.add(SaveStruct.load(f.getPath()));
				temp = new TextButton(gc, FontManager.DEFAULT_FONT, f.getName(), 0, 0,
						sbg, ID, new ButtonAction());
				temp.setAction(new SelectTextButtonAction(temp){
					public void activate(){
						nativeActivate();
						selectedButton = this.getButton();
						save = savedGames.get(gameList.getButtonIndex(selectedButton));
						minimap = save.getMap().getMinimap(200, 200);
					}
				});
				gameList.addButton(temp);
			}
		loadGameButton = new TextButton(gc, FontManager.BUTTON_FONT, "LOAD", 600, 370, sbg, ID, new UnImplementedAction());
		loadGameButton.setBorderEnabled(true);
		super.init(gc, sbg);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		gameList.render(container, g);
		loadGameButton.render(container, g);
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
		super.update(container, game, delta);
	}
	
	@Override
	public int getID(){
		return ID;
	}
	
}
