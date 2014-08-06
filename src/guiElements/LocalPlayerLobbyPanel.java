package guiElements;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import actions.GenericAction;
import actions.GenericIdAction;
import resourceManager.FontManager;
import theGame.GameLobbyState;
import theGame.Main;
import utils.Logger;
import gamePieces.Player;
import gamePieces.Race;

public class LocalPlayerLobbyPanel extends PlayerLobbyPanel{

	private static int lastId = 1;
	
	private Button colorBase; 
	private ExpandingMenu colorMenu;
	private Button raceBase;
	private ExpandingMenu raceMenu;
	
	private boolean reporting;
	
	public LocalPlayerLobbyPanel(GameLobbyState lby, Player p, int x, int y) throws SlickException{
		super(lby, p, x, y);
		GameContainer container = Main.getStaticContainer();
		lobby = lby;
		int nameHeight = nameFont.getHeight();
		int standardHeight = FontManager.BUTTON_TRUETYPE.getHeight();
		colorBase = new TextButton(container, FontManager.BUTTON_FONT, "Color",
				0, 0,
				Main.getGame(), lby.getID(),
				new GenericAction());
		colorMenu = new ExpandingMenu(container, lobby.getID(), colorBase, X, y+nameHeight+container.getHeight()/50, ExpandingMenu.DOWN, 5);
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
	public boolean mouseClick(int button, int x, int y) {
		return raceMenu.mouseClick(button, x, y) || colorMenu.mouseClick(button, x, y);
	}

	@Override
	public boolean mouseMove(int oldx, int oldy, int newx, int newy) {
		return raceMenu.mouseMove(oldx, oldy, newx, newy) || colorMenu.mouseMove(oldx, oldy, newx, newy);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		super.render(container, game, g);
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
	
}
