/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package resourceManager;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author support
 */
public class SoundManager {
    
    private static Sound menuMusic;
    public final int MAIN_MENU = 0;
    
    private SoundManager(){
        //enforce static class, never instantiate this
    }
    
    public static void playSound(int id){
        switch(id){
            case 0: menuMusic.play(); break;
            default: break;
        }
    }
    
    public static void loopSound(int id){
        switch(id){
            case 0: menuMusic.loop(); break;
            default: break;
        }
    }
    
    public static void stopSound(int id){
        switch(id){
            case 0: menuMusic.stop(); break;
            default: break;
        }
    }
    
    public static boolean isPlaying(int id){
        switch(id){
            case 0: return menuMusic.playing();
            default: return false;
        }
    }
            
    /**
     * Loads all resources used by the SoundManager
     * @throws org.newdawn.slick.SlickException when there are errors accessing the files
     */
    public static void init() throws SlickException{
        menuMusic = new Sound(ResourceLoader.getResourceAsStream("res/MainMenu.wav"), "Main Menu");
    }
}
