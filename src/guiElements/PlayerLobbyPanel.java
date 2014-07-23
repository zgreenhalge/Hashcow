package guiElements;

import gamePieces.Player;

public class PlayerLobbyPanel {

	private Player player;
	private int x;
	private int y;
	
	public PlayerLobbyPanel(Player p, int X, int Y){
		player = p;
		x = X;
		y = Y;
	}
	
	public Player getPlayer(){
		return player;
	}
}
