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

public class LocalPlayerLobbyPanel implements Component{

	private static TrueTypeFont nameFont = FontManager.NAME_TRUETYPE;
	private static PlayerColors colors = new PlayerColors();
	private static int lastId = 1;
	
	private String name;
	private GameLobbyState lobby;
	private Player player;
	private int X;
	private int Y;
	
	private Button colorBase; 
	private ExpandingMenu colorMenu;
	private Button raceBase;
	private ExpandingMenu raceMenu;
	private Image colorTile;
	
	private boolean hidden;
	private boolean reporting;
	
	public LocalPlayerLobbyPanel(GameLobbyState lby, Player p, int x, int y) throws SlickException{
		GameContainer container = Main.getStaticContainer();
		player = p;
		lobby = lby;
		X = x;
		Y= y;
		int nameHeight = nameFont.getHeight();
		int standardHeight = FontManager.BUTTON_TRUETYPE.getHeight();
		colorTile = new Image(nameHeight, nameHeight);
		colorBase = new TextButton(container, FontManager.BUTTON_FONT, "Color",
				0, 0,
				Main.getGame(), lby.getID(),
				new GenericAction());
		colorMenu = new ExpandingMenu(container, lobby.getID(), colorBase, X, y+nameHeight+container.getHeight()/50, ExpandingMenu.DOWN, 5);
		for(Color c: colors){
			colorMenu.addButton(new ImageButton(container, new SpriteSheet(colors.getColorAsImage(c, standardHeight, standardHeight), standardHeight, standardHeight, 1),
					0, 0,
					Main.getGame(),
					lby.getID(), 
					new GenericIdAction(colors.getId(c)){
				public void activate(){
					Color temp = colors.getColor(this.id);
					if(!lobby.isColorTaken(temp)){
						lobby.setColor(player, temp);
						colorTile = colors.getColorAsImage(temp, colorTile.getWidth(), colorTile.getHeight());
					}
				}
			}));
		}
		raceBase = new TextButton(container, FontManager.BUTTON_FONT, "Select Your Race...",
				0, 0,
				Main.getGame(), lby.getID(),
				new GenericAction());
		raceMenu = new ExpandingMenu(container, lobby.getID(), raceBase, X+colorMenu.getWidth()+container.getWidth()/50, y+nameHeight+container.getHeight()/50, ExpandingMenu.DOWN, 5);
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
		colorMenu.setReporting(true);
		raceMenu.setReporting(true);
		name = "LocalPlayerLobbyPanel" + (lastId++);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getWidth(){
		return (raceMenu.getX() + raceMenu.getWidth()) - X;
	}
	
	public int getHeight(){
		return (colorMenu.getY() + colorMenu.getHeight()) - Y;
	}
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
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
		raceMenu.setX(X + colorMenu.getWidth()+container.getWidth()/50);
		raceMenu.setY(Y + nameFont.getHeight() + container.getHeight()/50);
		colorMenu.setX(X);
		colorMenu.setY(Y + nameFont.getHeight() + container.getHeight()/50);
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
	public boolean contains(int x, int y) {
		if(x < X || x > X + getWidth())
			return false;
		if(y < Y || y > Y + getHeight())
			return false;
		return true;
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
		if(player.getRace() == null)
			nameFont.drawString(X + colorTile.getWidth() + container.getWidth()/25, Y, player.getName());
		else
			nameFont.drawString(X + colorTile.getWidth() + container.getWidth()/25, Y, player.getName() + " - " + player.getRace());
		colorMenu.render(container, game, g);
		raceMenu.render(container, game, g);
		colorTile.draw(X, Y);
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
	
}
