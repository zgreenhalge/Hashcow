package theGame;

import gamePieces.MapInfo;
import gamePieces.Player;
import guiElements.ExpandingMenu;
import guiElements.LocalPlayerLobbyPanel;
import guiElements.TextButton;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import actions.GenericAction;
import actions.GenericIdAction;
import actions.StateTransitionAction;
import resourceManager.FontManager;
import resourceManager.MapManager;
import utils.Logger;
import utils.OneToOneMap;

public class GameLobbyState extends HCGameState {

	public static final int ID = 2;
	
	private static GameLobbyState currentLobby;

	private Image minimap;
	private int minimapX;
	private int minimapY;
	
	private MapInfo selectedMap;
	private OneToOneMap<Player, Color> selectedColors;
	private ArrayList<LocalPlayerLobbyPanel> players;
	private ArrayList<MapInfo> maps;
	private ExpandingMenu mapMenu;
	private TextButton mapMenuBase;
	private TextButton startButton;
	private TextButton backButton;
	private TextButton addPlayerButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{
		super.init(container, game);
		int cWidth = container.getWidth();
		int cHeight = container.getHeight();
		LocalPlayerLobbyPanel temp;
		players = new ArrayList<LocalPlayerLobbyPanel>();
		temp = new LocalPlayerLobbyPanel(this, new Player(1), cWidth/20, cHeight/20);
		players.add(temp);
		selectedColors = new OneToOneMap<Player, Color>();
		maps = MapManager.loadMaps();
		if(cWidth < cHeight)
			minimap = new Image(cWidth/4, cWidth/4);
		else
			minimap = new Image(cHeight/4, cHeight/4);
		minimapX = 8*cWidth/9-minimap.getWidth();
		minimapY = cHeight/10;
		Font buttonFont = FontManager.BUTTON_FONT;
		mapMenuBase = new TextButton(container, buttonFont, "<Select A Map>",
				0, 0,
				game, this.getID(),
				new GenericAction());
		mapMenu = new ExpandingMenu(container, ID, mapMenuBase, 0, minimapY + minimap.getHeight() + cHeight/50, ExpandingMenu.DOWN, 10);
		mapMenu.setX(minimapX+((minimap.getWidth()-mapMenu.getWidth())/2));
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
				minimapX + minimap.getWidth()/2 + (minimap.getWidth()/2 - FontManager.BUTTON_TRUETYPE.getWidth("Back"))/2, minimapY + minimap.getHeight()*2 + 10,
				game, this.getID(), 
				new StateTransitionAction(game, MainMenuState.ID));
		addPlayerButton = new TextButton(container, buttonFont, "+",
				temp.getX() + temp.getWidth()/2, temp.getY() + temp.getHeight(),
				game, this.getID(), 
				new GenericAction(){
					public void activate(){
						LocalPlayerLobbyPanel temp = players.get(players.size()-1);
						try {
							players.add(new LocalPlayerLobbyPanel(currentLobby, new Player(players.size()), temp.getX() + temp.getWidth(), temp.getY() + temp.getHeight()));
							temp = players.get(players.size()-1);
							addPlayerButton.setLocation(temp.getX() + temp.getWidth()/2, temp.getY() + temp.getHeight());
						} catch (SlickException e) {
							Logger.loudLog(e);
							e.printStackTrace();
						}
					}
		});
		startButton = new TextButton(container, buttonFont, "Start",
				minimapX + (minimap.getWidth()/2 - FontManager.BUTTON_TRUETYPE.getWidth("Start"))/2, minimapY + minimap.getHeight()*2 + 10,
				game, this.getID(),
				new GenericAction(){
					public void activate(){
						if(selectedMap == null)
							return;
						ArrayList<Player> temp = getPlayers();
						for(Player p: temp)
							if(!p.ready())
								return;
						GameState gs = new GameState(selectedMap, temp);
						Main.getGame().addState(gs);
						
						Main.getGame().enterState(gs.getID());
					}
		});
		backButton.setBorderEnabled(true);
		startButton.setBorderEnabled(true);
		addPlayerButton.setBorderEnabled(true);
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		currentLobby = this;
	}
		
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		super.render(container, game, g);
		mapMenu.render(container, g);
		minimap.draw(minimapX, minimapY);
		startButton.render(container, g);
		backButton.render(container, g);
		addPlayerButton.render(container, g);
		for(LocalPlayerLobbyPanel panel: players)
			panel.render(container, g);
		
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
		for(LocalPlayerLobbyPanel p: players)
			ret.add(p.getPlayer());
		return ret;
	}

	public static void setLocal(){
		//set to local play
	}
	
	public static void setNetwork(){
		//set to network play
	}

	public void setColor(Player p, Color c){
		selectedColors.put(p, c);
		p.setColor(c);
	}

	public boolean isColorTaken(Color temp) {
		return selectedColors.containsValue(temp);
	}
	
	
}
