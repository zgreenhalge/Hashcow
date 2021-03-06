package theGame;

import gamePieces.MapInfo;
import gamePieces.Player;
import guiElements.ExpandingMenu;
import guiElements.LayeredGUI;
import guiElements.PlayerLobbyPanel;
import guiElements.PlayerLobbyPanel;
import guiElements.TextButton;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import networking.LobbyHost;

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
import resourceManager.ImageManager;
import resourceManager.MapManager;
import utils.Logger;
import utils.OneToOneMap;

public class GameLobbyState extends HCGameState {

	public static final int ID = 2;
	
	private static final int MAX_LOBBY = 8;
	
	private static GameLobbyState currentLobby;

	private Image minimap;
	private int minimapX;
	private int minimapY;
	
	private int numPlayers;
	
	private LayeredGUI gui;
	private MapInfo selectedMap;
	private OneToOneMap<Player, Color> selectedColors;
	private ArrayList<PlayerLobbyPanel> players;
	private ArrayList<MapInfo> maps;
	private ExpandingMenu mapMenu;
	private TextButton mapMenuBase;
	private TextButton startButton;
	private TextButton backButton;
	private TextButton addPlayerButton;
	private TextButton removePlayerButton;
	
	private static boolean network;
	private static boolean host;	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{
		super.init(container, game);
		int cWidth = container.getWidth();
		int cHeight = container.getHeight();
		players = new ArrayList<PlayerLobbyPanel>();
		selectedColors = new OneToOneMap<Player, Color>();
		Logger.loudLogLine("Loading maps...");
		maps = MapManager.loadMaps();
		
		Logger.loudLogLine("Creating minimap...");
		if(cWidth < cHeight)
			minimap = new Image(cWidth/4, cWidth/4);
		else
			minimap = new Image(cHeight/4, cHeight/4);
		minimapX = 8*cWidth/9-minimap.getWidth();
		minimapY = cHeight/20;
		
		Logger.loudLogLine("Creating map menu...");
		Font buttonFont = FontManager.BUTTON_FONT;
		mapMenuBase = new TextButton(container, buttonFont, "<Select A Map>",
				0, 0,
				game, this.getID(),
				new GenericAction());
		mapMenu = new ExpandingMenu(container, ID, mapMenuBase, 0, minimapY + minimap.getHeight() + cHeight/50, ExpandingMenu.DOWN, 10);
		mapMenu.setX(minimapX+((minimap.getWidth()-mapMenu.getWidth())/2));
		for(MapInfo map: maps){
			mapMenu.addButton(new TextButton(container, buttonFont, "(" + map.getMaxPlayers() + ") " + map.getName(),
					0, 0,
					game, this.getID(), 
					new GenericIdAction(maps.indexOf(map)){
						public void activate(){
							selectedMap = maps.get(this.id);
							minimap = selectedMap.getMinimap(minimap.getWidth(), minimap.getHeight());
						}
			}));
		}
		
		Logger.loudLogLine("Creating buttons...");
		removePlayerButton = new TextButton(container, buttonFont, " - ",
				0, 0,
				game, this.getID(), 
				new GenericAction(){
			public void activate(){
				removePlayer();
			}
		}); 
		addPlayerButton = new TextButton(container, buttonFont, " + ",
				0, 0,
				game, this.getID(), 
				new GenericAction(){
					public void activate(){
						addPlayer();
					}
		});
		backButton = new TextButton(container, buttonFont, "Back",
				minimapX + minimap.getWidth()/2 + (minimap.getWidth()/2 - FontManager.BUTTON_TRUETYPE.getWidth("Back"))/2, mapMenu.getY()+mapMenu.getHeight()+FontManager.BUTTON_TRUETYPE.getHeight()*4,
				game, this.getID(), 
				new StateTransitionAction(game, MainMenuState.ID));
		startButton = new TextButton(container, buttonFont, "Start",
				minimapX + (minimap.getWidth()/2 - FontManager.BUTTON_TRUETYPE.getWidth("Start"))/2, mapMenu.getY()+mapMenu.getHeight()+FontManager.BUTTON_TRUETYPE.getHeight()*4,
				game, this.getID(),
				new GenericAction(){
					public void activate(){
						ArrayList<Player> temp = getPlayers();
						for(Player p: temp)
							if(!p.ready())
								return;
						//TODO something in InGameState should be changed to enable network play
						InGameState gs = new InGameState(selectedMap, temp);
						Main.getGame().addState(gs);
						Main.getGame().enterState(gs.getID());
					}
		});
		
		Logger.loudLogLine("Creating player panel...");
		PlayerLobbyPanel temp;
		temp = new PlayerLobbyPanel(this, new Player(1), 0, 0);
		temp.setLocation((minimapX - cWidth/20 - temp.getWidth())/2, cHeight/20);
		numPlayers = 1;
		players.add(temp);
		int n = players.size();
		while(n < MAX_LOBBY){
			players.add(new PlayerLobbyPanel(this, new Player(++n), (minimapX - cWidth/20 - temp.getWidth())/2, (n)*(5*temp.getHeight()/4)));
			if(network)
				players.get(players.size()-1).setNetwork(true);
			players.get(n-1).setHidden(true);
		}
		
		
		addPlayerButton.setLocation(temp.getX() + 2*temp.getWidth()/3, temp.getY() + temp.getHeight() + temp.getHeight()/2);
		removePlayerButton.setLocation(temp.getX() + temp.getWidth()/3, temp.getY() + temp.getHeight() + temp.getHeight()/2);
		Logger.loudLogLine("State init complete.");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		super.enter(container, game);
		currentLobby = this;
		backButton.setBorderEnabled(true);
		startButton.setBorderEnabled(true);
		addPlayerButton.setBorderEnabled(true);
		removePlayerButton.setBorderEnabled(true);
		startButton.setEnabled(false);
		removePlayerButton.setEnabled(false);
		mapMenu.collapse();
		if(gui == null){
			gui = new LayeredGUI();
			gui.add(mapMenu, 0);
			if(host){
				gui.add(startButton, 1);
				gui.add(backButton, 1);
				gui.add(addPlayerButton, 9);
				gui.add(removePlayerButton, 9);
			}
			for(int i = players.size()-1; i>-1; i--)
				gui.add(players.get(i), i);
		}
	}
		
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		super.render(container, game, g);
		gui.render(container, game, g);
		minimap.draw(minimapX, minimapY);
		if(selectedMap != null){
			g.drawString(selectedMap.getName() + " (" + selectedMap.getWidth() + "x" + selectedMap.getHeight() + ")", mapMenu.getX(), mapMenu.getY() + mapMenu.getHeight() + FontManager.BUTTON_TRUETYPE.getHeight());
			g.drawString("Players: " + selectedMap.getMaxPlayers(), mapMenu.getX(), mapMenu.getY() + mapMenu.getHeight() + FontManager.BUTTON_TRUETYPE.getHeight()*2);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			game.enterState(MainMenuState.ID);
		evaluateStartButton();
		gui.update(container, game, delta);
		super.update(container, game, delta);
		
	}
	
	@Override
	public int getID(){
		return ID;
	}
	
	private ArrayList<Player> getPlayers(){
		ArrayList<Player> ret = new ArrayList<Player>();
		for(int i = 0; i<numPlayers; i++){
			if(network)
				if(players.get(i).getName().equals("Not Connected"))
					continue;
			ret.add(players.get(i).getPlayer());
		}
		return ret;
	}

	public static void setLocal(){
		network = false;
		host = true;
	}
	
	public static void setNetwork(boolean host){
		network = true;
		GameLobbyState.host = host;
	}

	public synchronized void setColor(Player p, Color c){
		selectedColors.put(p, c);
		p.setColor(c);
	}

	public synchronized boolean isColorTaken(Color temp) {
		return selectedColors.containsValue(temp);
	}
	
	public synchronized List<Integer> openPlayerSlots(){
		List<Integer> ret = new ArrayList<Integer>();
		for(PlayerLobbyPanel panel: players)
			if(panel.getPlayer().getName().equals("Not Connected") && !panel.isHidden())
				ret.add(players.indexOf(panel));
		return ret;
	}
	
	public synchronized PlayerLobbyPanel getPlayerPanel(int id){
		if(id <= 0 || id > players.size())
			return null;
		return players.get(id-1);
	}
	
	public void addPlayer(){
		PlayerLobbyPanel temp = players.get(numPlayers++);
		temp.setHidden(false);
		temp.setNetwork(network);
		addPlayerButton.setLocation(addPlayerButton.getX(), temp.getY() + temp.getHeight() + temp.getHeight()/2);
		removePlayerButton.setLocation(removePlayerButton.getX(), temp.getY() + temp.getHeight() + temp.getHeight()/2);
		if(numPlayers == MAX_LOBBY)
			addPlayerButton.setEnabled(false);
		removePlayerButton.setEnabled(true);
	}
	
	public void removePlayer(){
		PlayerLobbyPanel temp = players.get(--numPlayers);
		temp.setHidden(true);
		addPlayerButton.setLocation(addPlayerButton.getX(), temp.getY() + temp.getHeight()/2);
		removePlayerButton.setLocation(removePlayerButton.getX(), temp.getY() + temp.getHeight()/2);
		if(numPlayers == 1)
			removePlayerButton.setEnabled(false);
		addPlayerButton.setEnabled(true);		
	}
	
	public void startGame(LobbyHost host){
		host.closeLobby();
		startButton.getAction().activate();
	}
	
	public void closeLobby(){
		//set main menu to display closed lobby message
		//transition to main menu
	}
	
	private void evaluateStartButton(){
		if(selectedMap == null){
			startButton.setEnabled(false);
		}else if(numPlayers == 1){
			startButton.setEnabled(false);
		}else if(numPlayers <= selectedMap.getMaxPlayers()){
			for(PlayerLobbyPanel p: players){
				if(p.isNetwork()){
					if(!p.isReady() || !p.getPlayer().ready()){
						startButton.setEnabled(false);
						return;
					}
				}else if(!p.getPlayer().ready()){
					startButton.setEnabled(false);
					return;
				}
			}
			startButton.setEnabled(true);
		}else
			startButton.setEnabled(false);
	}
	
}
