package resourceManager;

import org.newdawn.slick.Sound;

import utils.Logger;

public class UnitSound {

	public static final int SELECT = 0;
	public static final int ATTACK = 1;
	public static final int MOVE = 2;
	public static final int DEATH = 3;
	
	private Sound attack;
	private Sound selected;
	private Sound movement;
	private Sound death;
	
	public UnitSound(String path){
		try{
			attack = new Sound(path +"/attack.wav");
		}catch(Exception e){
			Logger.log(e);
		}
		try{
			selected = new Sound(path +"/selected.wav");
		}catch(Exception e){
			Logger.log(e);
		}
		try{
			movement = new Sound(path +"/movement.wav");
		}catch(Exception e){
			Logger.log(e);
		}
		try{
			death = new Sound(path +"/death.wav");
		}catch(Exception e){
			Logger.log(e);
		}
	}
	
	public void playSound(int id){
		float vol = SoundManager.getVolume();
		float pitch = SoundManager.getPitch();
		switch(id){
			case SELECT: selected.play(pitch, vol); return;
			case ATTACK: attack.play(pitch, vol); return;
			case MOVE: movement.play(pitch, vol); return;
			case DEATH: death.play(pitch, vol); return;
			default: return;
		}
	}
	
	public void loopSound(int id){
		float vol = SoundManager.getVolume();
		float pitch = SoundManager.getPitch();
		switch(id){
			case SELECT: selected.loop(pitch, vol); return;
			case ATTACK: attack.loop(pitch, vol); return;
			case MOVE: movement.loop(pitch, vol); return;
			case DEATH: death.loop(pitch, vol); return;
			default: return;
		}
	}
	
	public void stopSound(int id){
		switch(id){
			case SELECT: selected.stop(); return;
			case ATTACK: attack.stop(); return;
			case MOVE: movement.stop(); return;
			case DEATH: death.stop(); return;
			default: return;
		}
	}
}
