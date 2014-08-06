package guiElements;

import gamePieces.Player;
import gamePieces.PlayerColors;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.FontManager;
import theGame.GameLobbyState;

public abstract class PlayerLobbyPanel implements Component {

	protected static PlayerColors playerColors = new PlayerColors();
	protected static TrueTypeFont nameFont = FontManager.NAME_TRUETYPE;
	
	protected String name;
	protected boolean hidden;
	protected Image colorTile;
	protected Player player;
	protected GameLobbyState lobby;
	protected int X;
	protected int Y;
	
	public PlayerLobbyPanel(GameLobbyState lby, Player p, int x, int y) throws SlickException{
		player = p;
		lobby = lby;
		X = x;
		Y = y;
		colorTile = new Image(nameFont.getHeight(), nameFont.getHeight());
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setPlayer(Player p){
		player = p;
	}
	
	public void setColor(Color c){
		lobby.setColor(player, c);
		colorTile = playerColors.getColorAsImage(c, colorTile.getWidth(), colorTile.getHeight());
	}
	
	public Color getColor(){
		return colorTile.getColor(0, 0);
	}
	
	@Override
	public void setName(String s){
		name = s;
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public int getX(){
		return X;
	}

	@Override
	public int getY(){
		return Y;
	}

	@Override
	public abstract int getWidth();

	@Override
	public abstract int getHeight();

	@Override
	public abstract void setLocation(int x, int y);

	@Override
	public void setHidden(boolean b) {
		hidden = b;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public abstract boolean isReporting();

	@Override
	public abstract void setReport(boolean b);

	@Override
	public boolean contains(int x, int y) {
		if(x < getX() || x > getX() + getWidth())
			return false;
		if(y < getY() || y > getY() + getHeight())
			return false;
		return true;
	}

	@Override
	public abstract boolean mouseClick(int button, int x, int y);

	@Override
	public abstract boolean mouseMove(int oldx, int oldy, int newx, int newy);

	@Override
	public abstract boolean mouseWheelMove(int change);

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(hidden)
			return;
		if(player.getRace() == null)
			nameFont.drawString(getX() + colorTile.getWidth() + container.getWidth()/25, getY(), player.getName());
		else
			nameFont.drawString(getX() + colorTile.getWidth() + container.getWidth()/25, getY(), player.getName() + " - " + player.getRace());
		colorTile.draw(getX(), getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta){}

}
