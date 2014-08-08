package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import theGame.GameLobbyState;
import utils.Logger;

public class LobbyHost extends Thread {

	private static final int PORT = 0; //automatically finds an open port - should find a static port eventually
	
	private static Object listLock = new Object();
	private static Object messageLock = new Object();
	
	private GameLobbyState lobby;
	private ArrayList<LobbyClient> clientList;
	private LinkedList<String> messages;
	//private ArrayList
	
	public LobbyHost(GameLobbyState lobby) throws IOException{
		this.lobby = lobby;
		clientList = new ArrayList<LobbyClient>();
		messages = new LinkedList<String>();
	}
	
	@Override
	public void run(){
		Thread greeter = new Thread(new LobbyGreeter());
		Thread messenger = new Thread(new LobbySender());
		greeter.start();
		messenger.start();
		while(true){
			if(Thread.interrupted()) //manual check for interrupt
				break;
			if(!greeter.isAlive())
				Logger.loudLog(new Exception("LobbyGreeter thread has died!")); //should do something more than log - maybe spin new one back up?
			if(!messenger.isAlive())
				Logger.loudLog(new Exception("LobbyMessenger thread has died!")); //should do something more than log - maybe spin new one back up?
			synchronized(listLock){
				for(LobbyClient client: clientList){
					if(client.in.hasNextLine())
						processMessage(client.in.nextLine(), client);
				}
			}
		}
		greeter.interrupt();
		messenger.interrupt();
	}
	
	private void processMessage(String nextLine, LobbyClient sender) {
		//read String
		//perform required Actions
		//send to outbound queue
	}

	public synchronized void startGame(){
		//clear outbound messages
		//tell all clients to start game
		//spin up game threads
	}
	
	public synchronized void closeLobby(){
		//clear outbound messages
		//tell all clients to exit lobby
		//spin down lobby threads
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
					temp = new LobbyClient(server.accept()); //accept new socket connection
					synchronized(listLock){					 //lock on the clientList lock
							List<Integer> open = lobby.openPlayerSlots();
							if(!clientList.contains(temp) && !open.isEmpty()){ //if the player hasn't connected already and we have an empty slot
								clientList.add(temp);
								temp.out.println(LobbyMessages.LOBBY_ACCEPT+"|"+open.get(0)); //tell client of acceptance, give client position in lobby
							}else 
								temp.out.println(LobbyMessages.LOBBY_FULL); //tell client the lobby is full
					}
				}
			} catch (IOException e) {
				Logger.loudLog(e);
				return; //must exit if there is an error establishing the server
			}
		}
		
	}

	/**
	 * A Runnable that will continually remove Strings from the message queue and send them to each LobbyClient
	 *
	 */
	private class LobbySender implements Runnable{

		@Override
		public void run() {
			String message;
			while(true){
				if(Thread.interrupted())
					return;
				while(messages.size() > 0){ //while there are messages in the list
					synchronized(messageLock){
						message = messages.remove(); //lock the list, then pull the front off
					}
					synchronized(listLock){
						for(LobbyClient client: clientList){	//send the message to every client
							client.out.println(message);
						}
					}
				}
			}
		}
	}
}
