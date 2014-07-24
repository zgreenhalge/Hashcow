package gamePieces;

/**
 * A test implementation of a Unit
 *
 */
public class TestUnit extends Unit {

	private static final long serialVersionUID = 8105115509694378024L;
	
	//move these to a unit database?				//
	private static final int BASE_MOVE_RANGE = 3;	//
	private static final int BASE_SIGHT_RANGE = 2;	//
	private static final int BASE_ATTACK_RANGE = 1;	//
	private static final int BASE_ATTACK = 1;		//	
	private static final int BASE_DEFENSE = 1;		//
	private static final int BASE_HEALTH = 10;		//
	private static final String name = "TestUnit";	//
	//move these to a unit database?				//
	
	public TestUnit(Coordinate location, Player owner, MapInfo map) {
		super(location, owner, map, MovementType.WALK);
		super.unitId = 0;
		setBaseMoveRange(BASE_MOVE_RANGE);
		setBaseSightRange(BASE_SIGHT_RANGE);
		setBaseAttackRange(BASE_ATTACK_RANGE);
		setBaseAttack(BASE_ATTACK);
		setBaseDefense(BASE_DEFENSE);
		setBaseHealth(BASE_HEALTH);
		setCurrentHealth(BASE_HEALTH);
		setName(name);
		load();
		owner.addUnit(this);
	}

}
