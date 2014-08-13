package networking;

public enum LobbyMessages {

	LOBBY_ACCEPT("~LobbyAccept"),
	LOBBY_FULL("~LobbyFull"),
	LOBBY_CLOSED("~LobbyClosed"),
	CHAT("~Chat"),
	CHANGE_COLOR("~ColorChange"),
	CHANGE_RACE("~RaceChange"),
	CHANGE_NAME("~NameChange"),
	QUIT_LOBBY("~QuitLobby"),
	READY("~Ready");
	
	private String message;
	
	private LobbyMessages(String m){
		message = m;
	}
	
	public String toString(){
		return message;
	}
}
