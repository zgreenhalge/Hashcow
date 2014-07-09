package gamePieces;

import resourceManager.TestUnitLibrary;
import resourceManager.UnitImage;

public class TestUnit extends Unit {
	
	{
	BASE_MOVE_RANGE = 3;
	BASE_SIGHT_RANGE = 2;
	BASE_ATTACK_RANGE = 1;
	BASE_ATTACK = 1;
	BASE_DEFENSE = 1;
	BASE_HEALTH = currentHealth = 10;
	healthString = currentHealth + "/" + BASE_HEALTH;
	}
	
	public TestUnit(int X, int Y, Player owner) {
		super(X, Y, owner);
		image = TestUnitLibrary.getUnitImage(TestUnitLibrary.MECH_LIGHT);
		sound = TestUnitLibrary.getUnitSound(TestUnitLibrary.MECH_LIGHT);
		current = image.getAnimation(UnitImage.IDLE);
	}

}
