package resourceManager;

import utils.Logger;

public class TestUnitLibrary extends UnitImageLibrary {

	private UnitImage mechLight;
	public static final int MECH_LIGHT = 0;
	
	public TestUnitLibrary(){
		try{
			mechLight = new UnitImage("res/images/units/mechLight");
		} catch(Exception e){
			Logger.loudLog(e);
		}
	}
	
	public UnitImage getUnitImage(int id) {
		switch(id){
			case MECH_LIGHT: return mechLight;
			default: return null;
		}
	}
	
}
