package resourceManager;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import utils.Logger;
import utils.Settings;

/**
 *
 * @author support
 */
public class SoundManager {
    
	private static SoundManager INSTANCE;
	
	private static float volume;
	private static float prevVolume;
	private static float pitch;
	private static boolean muted;
    private static Sound menuMusic;
    private static Sound buttonOver;
    private static Sound buttonClick;
    private static Sound buttonDisabled;
    public static final int MAIN_MENU = 0;
    public static final int BUTTON_OVER = 1;
    public static final int BUTTON_CLICK = 2;
    public static final int BUTTON_DISABLED = 3;
    
    public static final int[] sounds = {MAIN_MENU, BUTTON_OVER, BUTTON_CLICK, BUTTON_DISABLED};
     
    private SoundManager() throws SlickException{
    	volume = 1.0f;
    	pitch = 1.0f;
        menuMusic = new Sound("res/sounds/MainMenu.wav");
        buttonOver = new Sound("res/sounds/buttonOver.wav");
        buttonClick = new Sound("res/sounds/buttonClick.wav");
        buttonDisabled = new Sound("res/sounds/buttonDisabled.wav");
        muted = false;
        INSTANCE = this;
    }
    
    public static SoundManager getManager(){
    	if(INSTANCE == null)
			try {
				INSTANCE = new SoundManager();
			} catch (SlickException e) {
				Logger.log(e);
				e.printStackTrace();
			}
    	return INSTANCE;
    }
    
    public static void setVolume(float f){
    	if(f <= 1.0f && f >= 0f)
    		volume = f;
    	Settings.set(Settings.VOLUME, Float.toString(volume));
    }
    
    public static float getVolume(){
    	return volume;
    }
    
    public static void setPitch(float f){
    	if(f <= 1.0f && f >= 0f)
    		pitch = f;
    }
    
    public static float getPitch(){
    	return pitch;
    }
    
    public void playSound(int id){
    	if(INSTANCE == null) return;
        switch(id){
            case MAIN_MENU: menuMusic.play(pitch, volume); break;
            case BUTTON_OVER: buttonOver.play(pitch, volume); break;
            case BUTTON_CLICK: buttonClick.play(pitch, volume); break;
            case BUTTON_DISABLED: buttonDisabled.play(pitch, volume); break;
            default: break;
        }
    }
    
    public void playSound(int id, int pitch, int volume){
    	if(INSTANCE == null) return;
        switch(id){
            case MAIN_MENU: menuMusic.play(pitch, volume); break;
            case BUTTON_OVER: buttonOver.play(pitch, volume); break;
            case BUTTON_CLICK: buttonClick.play(pitch, volume); break;
            case BUTTON_DISABLED: buttonDisabled.play(pitch, volume); break;
            default: break;
        }
    }
    
    public void loopSound(int id){
    	if(INSTANCE == null) return;
        switch(id){
            case MAIN_MENU: menuMusic.loop(pitch, volume); break;
            case BUTTON_OVER: buttonOver.loop(pitch, volume); break;
            case BUTTON_CLICK: buttonClick.loop(pitch, volume); break;
            case BUTTON_DISABLED: buttonDisabled.loop(pitch, volume); break;
            default: break;
        }
    }
    
    public void loopSound(int id, int pitch, int volume){
    	if(INSTANCE == null) return;
        switch(id){
            case MAIN_MENU: menuMusic.loop(pitch, volume); break;
            case BUTTON_OVER: buttonOver.loop(pitch, volume); break;
            case BUTTON_CLICK: buttonClick.loop(pitch, volume); break;
            case BUTTON_DISABLED: buttonDisabled.loop(pitch, volume); break;
            default: break;
        }
    }
    
    public void stop(int id){
    	if(INSTANCE == null) return;
        switch(id){
            case MAIN_MENU: menuMusic.stop(); break;
            case BUTTON_OVER: buttonOver.stop(); break;
            case BUTTON_CLICK: buttonClick.stop(); break;
            case BUTTON_DISABLED: buttonDisabled.stop(); break;
            default: break;
        }
    }
    
    public static void stopAll(){
    	for(Integer i: sounds)
    		INSTANCE.stop(i);
    }
    
    public void mute(){
    	if((muted = !muted)){
	    	prevVolume = volume;
	    	volume = 0;
    	}else{
    		volume = prevVolume;
    	}
    
    }
    
    public boolean isPlaying(int id){
    	if(INSTANCE == null) return false;
        switch(id){
            case MAIN_MENU: return menuMusic.playing();
            case BUTTON_OVER: return buttonOver.playing();
            case BUTTON_CLICK: return buttonClick.playing();
            case BUTTON_DISABLED: return buttonDisabled.playing();
            default: return false;
        }
    }
            
}
