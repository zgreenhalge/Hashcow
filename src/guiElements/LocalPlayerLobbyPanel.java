package guiElements;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

import actions.GenericAction;
import actions.GenericIdAction;
import resourceManager.FontManager;
import theGame.GameLobbyState;
import theGame.GameState;
import theGame.Main;
import gamePieces.Player;
import gamePieces.PlayerColors;
import gamePieces.Race;

public class LocalPlayerLobbyPanel {

	private static TrueTypeFont nameFont = FontManager.NAME_TRUETYPE;
	private static PlayerColors colors = new PlayerColors();
	
	private GameLobbyState lobby;
	private Player player;
	private int X;
	private int Y;
	
	private Button colorBase; 
	private ExpandingMenu colorMenu;
	private Button raceBase;
	private ExpandingMenu raceMenu;
	private Image colorTile;
	
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
		raceBase = new TextButton(container, FontManager.BUTTON_FONT, "Select Your Race...",
				0, 0,
				Main.getGame(), lby.getID(),
				new GenericAction());
		}
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
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getWidth(){
		return (raceMenu.getX() + raceMenu.getWidth()) - X;
	}
	
	public int getHeight(){
		return nameFont.getHeight() + (colorMenu.getY() + colorMenu.getHeight() - Y);
	}
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
	}
	
	public void render(GameContainer container, Graphics g){
		if(player.getRace() == null)
			nameFont.drawString(X + colorTile.getWidth() + container.getWidth()/25, Y, player.getName());
		else
			nameFont.drawString(X + colorTile.getWidth() + container.getWidth()/25, Y, player.getName() + " - " + player.getRace());
		colorMenu.render(container, g);
		raceMenu.render(container, g);
		colorTile.draw(X, Y);
	}
	
}
