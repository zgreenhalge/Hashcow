package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import theGame.GameLobbyState;
import utils.Logger;

public class LobbyHost extends Thread {

	private static final int PORT = 0; //automatically finds an open port - should find a static port eventually
	
	private static Object listLock = new Object();
	private static Object outboundLock = new Object();
	
	private GameLobbyState lobby;
	private ArrayList<LobbyClient> clientList;
	private ArrayList<String> outbound;
	//private ArrayList
	
	public LobbyHost(GameLobbyState lobby) throws IOException{
		this.lobby = lobby;
		clientList = new ArrayList<LobbyClient>();
		outbound = new ArrayList<String>();
	}
	
	@Override
	public void run(){
		Thread greeter = new Thread(new LobbyGreeter());
		Thread messenger = new Thread();
		greeter.start();
		messenger.start();
		while(true){
			if(Thread.interrupted()) //manual check for interrupt
				break;
			synchronized(listLock){
				for(LobbyClient client: clientList){
					if(client.in.hasNextLine())
						processMessage(client.in.nextLine());
				}
			}
		}
		greeter.interrupt();
		messenger.interrupt();
	}
	
	private void processMessage(String nextLine) {
		//read String
		//perform required Actions
		//send to outbound queue
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
				LobbyClient temp;
				while(true){
					if(Thread.interrupted()){
						server.close();
						return;
					}
					temp = new LobbyClient(server.accept());
					synchronized(listLock){
						List<Integer> open = lobby.openPlayerSlots();
						if(!clientList.contains(temp) && !open.isEmpty()){
							clientList.add(temp);
							temp.out.println(LobbyMessages.LOBBY_ACCEPT+":"+open.get(0));
						}else 
							temp.out.println(LobbyMessages.LOBBY_FULL);
					}
				}
			} catch (IOException e) {
				Logger.loudLog(e);
				return; //must exit if there is an error establishing the server
			}
		}
		
	}

}
