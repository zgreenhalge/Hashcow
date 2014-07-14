package gamePieces;

import resourceManager.TestUnitLibrary;
import resourceManager.UnitImage;

/**
 * A test implementation of a Building
 *
 */
public class TestHQ extends Building {

	public TestHQ(Coordinate location, Player owner, MapInfo map) {
		super(location, owner, map);
		super.image = TestUnitLibrary.getUnitImage(TestUnitLibrary.TEST_HQ);
		super.sound = TestUnitLibrary.getUnitSound(TestUnitLibrary.TEST_HQ);
		super.current = image.getAnimation(UnitImage.IDLE);
		super.name = "Test HQ";
		setGloballyVisible(true);
	}

}
