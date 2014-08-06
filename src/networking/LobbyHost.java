package networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import theGame.GameLobbyState;
import utils.Logger;

public class LobbyHost extends Thread {

	private static final int PORT = 0; //automatically finds an open port - should find a static port eventually
	
	private static Object lock = new Object();
	
	private GameLobbyState lobby;
	private ArrayList<Socket> clientList;
	private boolean lobbyOpen;
	
	public LobbyHost(GameLobbyState lobby) throws IOException{
		this.lobby = lobby;
		lobbyOpen = true;
	}
	
	@Override
	public void run(){
		Thread greeter = new Thread(new LobbyGreeter());
		greeter.start();
		while(lobbyOpen){
			if(Thread.interrupted()){ //this should never happen, but we should be alerted if it does
				Logger.loudLogLine("Lobby thread " + this.getId() + " interrupted.");
				return;
			}
			synchronized(lock){
				
			}
		}
	}
	
	public synchronized void startGame(){
		
	}
	
	public synchronized void closeLobby(){
		
	}

	/**
	 * A Runnable that will listen on the specified port and accept new players until the Lobby is full
	 *
	 */
	private class LobbyGreeter implements Runnable{
		
		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(PORT);
				Socket temp;
				while(true){
					if(Thread.interrupted()){
						server.close();
						return;
					}
					temp = server.accept();
					synchronized(lock){
						List<Integer> open = lobby.openPlayerSlots();
						PrintWriter out = new PrintWriter(temp.getOutputStream(), true);
						if(!clientList.contains(temp) && !open.isEmpty()){
							clientList.add(temp);
							out.println(LobbyMessages.LOBBY_ACCEPT+":"+open.get(0));
						}else 
							out.println(LobbyMessages.LOBBY_FULL);
						out.close();
					}
				}
			} catch (IOException e) {
				Logger.loudLog(e);
				return; //must exit if there is an error establishing the server
			}
		}
		
	}

}
