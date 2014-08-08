package guiElements;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import actions.GenericAction;
import actions.GenericIdAction;
import resourceManager.FontManager;
import theGame.GameLobbyState;
import theGame.Main;
import utils.Logger;
import gamePieces.Player;
import gamePieces.PlayerColors;
import gamePieces.Race;

public class PlayerLobbyPanel implements Component{

	private static int lastId = 1;
	private static TrueTypeFont nameFont = FontManager.NAME_TRUETYPE;
	private static PlayerColors playerColors = new PlayerColors();
	
	private GameLobbyState lobby;
	private Player player;
	
	private String name;
	private int X;
	private int Y;
	
	private Image colorTile;
	private Button colorBase; 
	private ExpandingMenu colorMenu;
	private Button raceBase;
	private ExpandingMenu raceMenu;
	
	private boolean hidden;
	private boolean reporting;
	private boolean network;
	
	public PlayerLobbyPanel(GameLobbyState lby, Player p, int x, int y) throws SlickException{
		GameContainer container = Main.getStaticContainer();
		lobby = lby;
		player = p;
		X = x;
		Y = y;
		int nameHeight = nameFont.getHeight();
		int standardHeight = FontManager.BUTTON_TRUETYPE.getHeight();
		colorTile = new Image(nameHeight, nameHeight);
		colorBase = new TextButton(container, FontManager.BUTTON_FONT, "Color",
				0, 0,
				Main.getGame(), lby.getID(),
				new GenericAction());
		colorMenu = new ExpandingMenu(container, lobby.getID(), colorBase, X, y+(3*nameHeight/2), ExpandingMenu.DOWN, 5);
		for(Color c: playerColors){
			colorMenu.addButton(new ImageButton(container, new SpriteSheet(playerColors.getColorAsImage(c, standardHeight, standardHeight), standardHeight, standardHeight, 1),
					0, 0,
					Main.getGame(),
					lby.getID(), 
					new GenericIdAction(playerColors.getId(c)){
				public void activate(){
					Color temp = playerColors.getColor(this.id);
					if(!lobby.isColorTaken(temp)){
						setColor(temp);
					}
				}
			}));
		}
		raceBase = new TextButton(container, FontManager.BUTTON_FONT, "Select Your Race...",
				0, 0,
				Main.getGame(), lby.getID(),
				new GenericAction());
		raceMenu = new ExpandingMenu(container, lobby.getID(), raceBase, X+colorMenu.getWidth()+container.getWidth()/40, y+(3*nameHeight/2), ExpandingMenu.DOWN, 5);
		for(Race r: Race.values())
			raceMenu.addButton(new TextButton(container,
					FontManager.BUTTON_FONT, r.toString(),
					0, 0,
					Main.getGame(),
					lby.getID(), 
					new GenericIdAction(r.getId()){
						public void activate(){
							player.setRace(Race.getRace(this.id));
						}
			}));
		colorMenu.collapse();
		raceMenu.collapse();
		colorMenu.setReporting(true);
		raceMenu.setReporting(true);
		name = "LocalPlayerLobbyPanel" + (lastId++);
	}
	
	public int getWidth(){
		return (raceMenu.getX() + raceMenu.getWidth()) - X;
	}
	
	public int getHeight(){
		return (colorMenu.getY() + colorMenu.getHeight()) - Y;
	}
	
	public void setHidden(boolean b){
		hidden = b;
		colorMenu.setHidden(b);
		raceMenu.setHidden(b);
	}
	
	public boolean isHidden(){
		return hidden;
	}
	
	public void setLocation(int X, int Y){
		GameContainer container = Main.getStaticContainer();
		this.X = X;
		this.Y = Y;
		colorMenu.setX(X);
		colorMenu.setY(Y + (3*nameFont.getHeight()/2));
		raceMenu.setX(X + colorMenu.getWidth()+container.getWidth()/40);
		raceMenu.setY(Y + (3*nameFont.getHeight()/2));
	}

	@Override
	public void setName(String s) {
		name = s;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isReporting() {
		return reporting;
	}

	@Override
	public void setReport(boolean b) {
		reporting = b;
		raceMenu.setReport(b);
		colorMenu.setReport(b);
	}

	@Override
	public boolean mouseClick(int button, int x, int y) {
		return raceMenu.mouseClick(button, x, y) || colorMenu.mouseClick(button, x, y);
	}

	@Override
	public boolean mouseMove(int oldx, int oldy, int newx, int newy) {
		return raceMenu.mouseMove(oldx, oldy, newx, newy) || colorMenu.mouseMove(oldx, oldy, newx, newy);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(hidden)
			return;
		colorTile.draw(X, Y);
		if(player.getRace() == null)
			g.drawString(player.getName(), X + 3*colorTile.getWidth()/2, Y);
		else
			g.drawString(player.getName() + " - " + player.getRace(), X + 3*colorTile.getWidth()/2, Y);
		colorMenu.render(container, game, g);
		raceMenu.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		raceMenu.update(container, game, delta);
		colorMenu.update(container, game, delta);
	}

	@Override
	public boolean mouseWheelMove(int change) {
		if(hidden)
			return false;
		return raceMenu.mouseWheelMove(change) || colorMenu.mouseWheelMove(change);
	}

	@Override
	public int getX() {
		return X;
	}

	@Override
	public int getY() {
		return Y;
	}
	
	public void setPlayer(Player p){
		player = p;
	}
	
	public Player getPlayer(){
		return player;
	}

	@Override
	public boolean contains(int x, int y) {
		if(x > X && x < X + getWidth())
			if(y > Y && y < Y + getHeight())
				return true;
		return false;
	}
	
	public void setNetwork(boolean b){
		colorMenu.setHidden(b);
		raceMenu.setHidden(b);
		network = b;
	}
	
	public void setColor(Color c){
		lobby.setColor(player, c);
		colorTile = playerColors.getColorAsImage(c, colorTile.getWidth(), colorTile.getHeight());
	}
	
	public void clear(){
		if(network)
			player.setName("Not Connected");
		else
			player.setName("Player " + player.getId());
		player.setRace(null);
		player.setColor(playerColors.getColor(-1)); //clear
	}
	
}
