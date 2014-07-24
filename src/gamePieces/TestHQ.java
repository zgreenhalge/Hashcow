package gamePieces;

/**
 * A test implementation of a Building
 *
 */
public class TestHQ extends Building {

	private static final long serialVersionUID = -38575610549992088L;

	public TestHQ(Coordinate location, Player owner, MapInfo map) {
		super(location, owner, map, MovementType.IMMOBILE);
		super.name = "Test HQ";
		super.unitId = 1;
		load();
		setGloballyVisible(true);
	}

}
