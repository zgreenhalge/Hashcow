package gamePieces;

public enum TileTemplate {

	GRASS("Grass", new int[] {1, 1, 1, 1, 1}, true, "G");
	
	public String name;
	public int[] moveCost;
	public boolean buildable;
	public String fileName;
	
	private TileTemplate(String s, int[] costs, boolean build, String fName){
		name = s;
		moveCost = costs;
		buildable = build;
		fileName = fName;
	}
	
	public static TileTemplate getTemplate(String s){
		switch(s){
			case "G": return GRASS;
			default: return GRASS;
		}
	}
}
