package gamePieces;

public enum Race {

	HUMAN(1, "HUMAN"),
	ANDROID(2, "ANDROID"),
	BUG(3, "BUG"),
	TEST(0, "TEST");
	
	private int id;
	private String name;
	
	private Race(int id, String n){
		this.id = id;
		name = n;
	}
	
	public int getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public static Race getRace(int id){
		switch(id){
			case 0: return TEST;
			case 1: return HUMAN;
			case 2: return BUG;
			default: return null;
		}
	}
	
}
