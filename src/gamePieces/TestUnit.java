package gamePieces;

import resourceManager.TestUnitLibrary;
import resourceManager.UnitImage;

public class TestUnit extends Unit {
	
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
		super(location, owner, map);
		super.image = TestUnitLibrary.getUnitImage(TestUnitLibrary.MECH_LIGHT);
		super.sound = TestUnitLibrary.getUnitSound(TestUnitLibrary.MECH_LIGHT);
		super.current = image.getAnimation(UnitImage.IDLE);
		setBaseMoveRange(BASE_MOVE_RANGE);
		setBaseSightRange(BASE_SIGHT_RANGE);
		setBaseAttackRange(BASE_ATTACK_RANGE);
		setBaseAttack(BASE_ATTACK);
		setBaseDefense(BASE_DEFENSE);
		setBaseHealth(BASE_HEALTH);
		setCurrentHealth(BASE_HEALTH);
		refreshHealthString();
		setName(name);
		owner.addUnit(this);
	}

}
