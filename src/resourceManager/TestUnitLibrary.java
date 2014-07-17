package resourceManager;

import utils.Logger;

public class TestUnitLibrary{

	private static UnitImage mechLightImage;
	private static UnitSound mechLightSound;
	private static UnitImage testBuildingImage;
	private static UnitSound testBuildingSound;
	
	public static final int TEST_UNIT = 0;
	public static final int TEST_HQ = 1;
	
	private TestUnitLibrary(){}
	
	public static UnitImage getUnitImage(int id) {
		switch(id){
			case TEST_UNIT:
				if(mechLightImage == null){
					try{
						mechLightImage = new UnitImage("res/images/units/mechLight");
					} catch(Exception e){
						Logger.loudLog(e);
					}
				}
				return mechLightImage;
			case TEST_HQ:
				if(testBuildingImage == null){
					try{
						testBuildingImage = new UnitImage("res/images/buildings/mech.HQ");
					} catch(Exception e){
						Logger.loudLog(e);
					}
				}
				return testBuildingImage;
			default: return null;
		}
	}
	
	public static UnitSound getUnitSound(int id){
		switch(id){
		case TEST_UNIT:
			if(mechLightSound == null){
				try{
					mechLightSound = new UnitSound("res/sounds/units/mechLight");
				} catch(Exception e){
					Logger.loudLog(e);
				}
			}
			return mechLightSound;
		case TEST_HQ:
			if(testBuildingSound == null){
				try{
					testBuildingSound = new UnitSound("res/sounds/buildings/mech.HQ");
				} catch(Exception e){
					Logger.loudLog(e);
				}
			}
			return testBuildingSound;
		default: return null;
		}
	}
	
}
