/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package resourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author support
 */
public class SoundManager {
    
	private static SoundManager INSTANCE;
	
	private static float volume;
	private static float pitch;
    private static Sound menuMusic;
    public static final int MAIN_MENU = 0;
    
    
    public SoundManager(GameContainer container) throws SlickException{
    	volume = container.getMusicVolume();
    	pitch = 1.0f;
        menuMusic = new Sound("res/MainMenu.wav");
        INSTANCE = this;
    }
    
    /**
     * Retrieve the current SoundManager
     * @return the SoundManager Instance currently in use
     */
    public static SoundManager getInstance(){
    	return INSTANCE;
    }
    
    public void setVolume(float f){
    	if(f <= 1.0f && f >= 0f)
    		volume = f;
    }
    
    public float getVolume(){
    	return volume;
    }
    
    public void setPitch(float f){
    	if(f <= 1.0f && f >= 0f)
    		pitch = f;
    }
    
    public float getPitch(){
    	return pitch;
    }
    
    public void playSound(int id){
        switch(id){
            case 0: menuMusic.play(pitch, volume); break;
            default: break;
        }
    }
    
    public void playSound(int id, int pitch, int volume){
        switch(id){
            case 0: menuMusic.play(pitch, volume); break;
            default: break;
        }
    }
    
    public void loopSound(int id){
        switch(id){
            case 0: menuMusic.loop(pitch, volume); break;
            default: break;
        }
    }
    
    public void loopSound(int id, int pitch, int volume){
        switch(id){
            case 0: menuMusic.loop(pitch, volume); break;
            default: break;
        }
    }
    
    public void stopSound(int id){
        switch(id){
            case 0: menuMusic.stop(); break;
            default: break;
        }
    }
    
    public boolean isPlaying(int id){
        switch(id){
            case 0: return menuMusic.playing();
            default: return false;
        }
    }
            
}
