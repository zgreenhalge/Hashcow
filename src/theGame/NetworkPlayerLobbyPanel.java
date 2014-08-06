package theGame;

import org.newdawn.slick.SlickException;

import gamePieces.Player;
import guiElements.PlayerLobbyPanel;

public class NetworkPlayerLobbyPanel extends PlayerLobbyPanel {

	public NetworkPlayerLobbyPanel(GameLobbyState lby, Player p, int x, int y) throws SlickException{
		super(lby, p, x, y);
	}

	@Override
	public void setName(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isReporting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReport(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean mouseClick(int button, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMove(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseWheelMove(int change) {
		// TODO Auto-generated method stub
		return false;
	}

}
