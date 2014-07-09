package resourceManager;

import utils.Logger;

public class TestUnitLibrary{

	private static UnitImage mechLightImage;
	private static UnitSound mechLightSound;
	public static final int MECH_LIGHT = 0;
	
	private TestUnitLibrary(){}
	
	public static UnitImage getUnitImage(int id) {
		switch(id){
			case MECH_LIGHT:
				if(mechLightImage == null){
					try{
						mechLightImage = new UnitImage("res/images/units/mechLight");
					} catch(Exception e){
						Logger.loudLog(e);
					}
				}
				return mechLightImage;
			default: return null;
		}
	}
	
	public static UnitSound getUnitSound(int id){
		switch(id){
		case MECH_LIGHT:
			if(mechLightSound == null){
				try{
					mechLightSound = new UnitSound("res/sounds/units/mechLight");
				} catch(Exception e){
					Logger.loudLog(e);
				}
			}
			return mechLightSound;
			default: return null;
		}
	}
	
}
