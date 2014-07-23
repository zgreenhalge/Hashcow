package theGame;

import gamePieces.MapInfo;
import gamePieces.Player;
import guiElements.ExpandingMenu;
import guiElements.PlayerLobbyPanel;
import guiElements.TextButton;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.FontManager;
import resourceManager.MapManager;
import utils.Logger;
import actions.GenericAction;
import actions.GenericIdAction;
import actions.StateTransitionAction;

public class GameLobbyState extends HCGameState {

	public static final int ID = 2;

	private Image minimap;
	private int minimapX;
	private int minimapY;
	
	private MapInfo selectedMap;
	
	private ArrayList<PlayerLobbyPanel> players;
	private ArrayList<MapInfo> maps;
	private ExpandingMenu mapMenu;
	private TextButton mapMenuBase;
	private TextButton startButton;
	private TextButton backButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{
		super.init(container, game);
		int cWidth = container.getWidth();
		int cHeight = container.getHeight();
		players = new ArrayList<PlayerLobbyPanel>();
		maps = MapManager.loadMaps();
		minimap = new Image(250, 250);
		minimapX = 4*cWidth/5-minimap.getWidth();
		minimapY = cHeight/5;
		Font buttonFont = FontManager.BUTTON_FONT;
		mapMenuBase = new TextButton(container, buttonFont, "<Select A Map>",
				0, 0,
				game, this.getID(),
				new GenericAction());
		mapMenu = new ExpandingMenu(container, mapMenuBase, minimapX + 45, minimapY + minimap.getHeight() + 10, ExpandingMenu.DOWN, 10);
		for(MapInfo map: maps){
			mapMenu.addButton(new TextButton(container, buttonFont, "(" + map.getMaxPlayers() + ")" + map.getName(),
					0, 0,
					game, this.getID(), 
					new GenericIdAction(maps.indexOf(map)){
						public void activate(){
							selectedMap = maps.get(this.id);
							minimap = selectedMap.getMinimap(minimap.getWidth(), minimap.getHeight());
						}
			}));
		}
		backButton = new TextButton(container, buttonFont, "Back",
				minimapX + minimap.getWidth() - FontManager.BUTTON_TRUETYPE.getWidth("Back") - 10, minimapY + minimap.getHeight()*2 + 10,
				game, this.getID(), 
				new StateTransitionAction(game, MainMenuState.ID));
		startButton = new TextButton(container, buttonFont, "Start",
				minimapX + 10, minimapY + minimap.getHeight()*2 + 10,
				game, this.getID(),
				new GenericAction(){
					public void activate(){
						if(selectedMap == null)
							return;
						GameState gs = new GameState(selectedMap, getPlayers());
						Main.getGame().addState(gs);
						
						Main.getGame().enterState(gs.getID());
					}
		});
		backButton.setBorderEnabled(true);
		startButton.setBorderEnabled(true);
	}
	
	
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		super.render(container, game, g);
		mapMenu.render(container, g);
		minimap.draw(minimapX, minimapY);
		startButton.render(container, g);
		backButton.render(container, g);
		
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			game.enterState(MainMenuState.ID);
		super.update(container, game, delta);
		
	}
	
	@Override
	public int getID(){
		return ID;
	}
	
	private ArrayList<Player> getPlayers(){
		ArrayList<Player> ret = new ArrayList<Player>();
		for(PlayerLobbyPanel p: players)
			ret.add(p.getPlayer());
		return ret;
	}

	public static void setLocal(){
		//set to local play
	}
	
	public static void setNetwork(){
		//set to network play
	}
	
	
}
