package guiElements;
     
import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

import actions.Action;
import resourceManager.SoundManager;
import utils.Logger;
     
/**
 * A Button based on a String
 */
public class TextButton extends MouseOverArea implements Button{
    	     
    private TrueTypeFont ttfont;
    private String text;
    private boolean enabled = true;
    private boolean lastMouseOver = false;
    private StateBasedGame sbg;
    private int stateID;
    private TrueTypeFont biggerFont;
    private Action action;
    private boolean borderEnabled;
    private Color borderColor;
    private String name;
    private int oldX, oldY, bigX, bigY;
    private boolean report = false;
    private boolean hidden;
 
    public TextButton(GameContainer gc, Font font, String text, int x, int y, StateBasedGame sbg, int stateID, Action action) throws SlickException {
    	super((GUIContext)gc, new Image(0, 0), x, y, (new TrueTypeFont(font, false)).getWidth(text), (new TrueTypeFont(font, false)).getHeight());
    	this.ttfont = new TrueTypeFont(font, false);
        this.text = text;
        this.sbg = sbg;
        this.stateID = stateID;
        this.action = action;
        hidden = false;
        borderColor = Color.darkGray;
        biggerFont = new TrueTypeFont(new Font(font.getFontName(), Font.BOLD, font.getSize()*(12/10)), false);
        name = text.replaceAll(" ", "") + "Button";
        oldX = x;
        oldY = y;
        bigX = x - (biggerFont.getWidth(text) - ttfont.getWidth(text))/2;
        bigY = y - (biggerFont.getLineHeight() - ttfont.getLineHeight())/2;
    }
    
    
    public void setHidden(boolean b){
    	hidden = b;
    	setAcceptingInput(!b);
    }
    
    public boolean isHidden(){
    	return hidden;
    }
 
    public void setEnabled(boolean b) {
        enabled = b;
    }
 
    public boolean isEnabled() {
        return enabled;
    }
 
    public void setBorderEnabled(boolean b){
    	borderEnabled = b;
    }
    
    public boolean isBorderEnabled(){
    	return borderEnabled;
    }
    
    public void setAction(Action a){
    	action = a;
    }
    
    public Action getAction(){
    	return action;
    }
    
    public void setColor(Color c){
    	borderColor = c;
    }
    
    @Override
    public String getName() {
    	return name;
    }
    
    @Override
    public Button setName(String s) {
    	name = s;
    	return this;
    }
    
    public void setReport(boolean b){
    	report = b;
    }
    
    public boolean isReporting(){
    	return report;
    }
    
    public int getWidth(){
    	return biggerFont.getWidth(text);
    }
    
    public int getHeight(){
    	return biggerFont.getHeight();
    }

    @Override
    public void render(GUIContext guic, Graphics g) {
    	if(!hidden){
        	org.newdawn.slick.Font prevFont = g.getFont();
            g.setFont(ttfont);
            Color standard = g.getColor();
            if(enabled){
                g.setColor(Color.orange.brighter(.5f));
                if (isMouseOver()) {
                    g.setFont(biggerFont);
                    g.setColor(new Color(200, 50, 30));
                    setX(bigX);
                    setY(bigY);
                }else { 
                	setX(oldX);
                    setY(oldY);
                }
            }else{
                g.setColor(Color.gray);
            }
            if(borderEnabled){
            	int pad= ttfont.getHeight() / 5;
            	int height = ttfont.getLineHeight();
            	int width;
            	if(isMouseOver()){
            		width = biggerFont.getWidth(text);
            	}else{
	            	width = ttfont.getWidth(text);
            	}
            	Color prevCol = g.getColor();
            	g.setColor(borderColor);
            	g.drawLine(getX()-pad, getY()-pad, getX()+width+pad, getY()-pad);//top left to top right
            	g.drawLine(getX()-pad, getY()-pad, getX()-pad, getY()+height+pad);//top left to bottom left
            	g.drawLine(getX()-pad, getY()+height+pad, getX()+width+pad, getY()+height+pad);//bottom left to bottom right
            	g.drawLine(getX()+width+pad, getY()+height+pad, getX()+width+pad, getY()-pad);//bottom right to top right
            	g.setColor(prevCol);
            }
            g.drawString(text, getX(), getY());
            super.render(guic, g);
            g.setColor(standard);
            g.setFont(prevFont);
    	}
    }
 
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (sbg.getCurrentStateID() == stateID && !hidden){//if in proper state & button is active
            if (isMouseOver() && !lastMouseOver){//if mouse is over && wasn't previously over
                SoundManager.getManager().playSound(SoundManager.BUTTON_OVER); //play sound
                lastMouseOver = true;	//make sure sound won't repeat
            } else if (!isMouseOver()) { //if mouse is not over button
                lastMouseOver = false;	//allow sound to be played again
            }
        }
        super.mouseMoved(oldx, oldy, newx, newy);	//pass to super
    }
 
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    	if(hidden)
    		return;
    	if(enabled){
    		if (isMouseOver() && sbg.getCurrentStateID() == stateID) {
    			if(report) 
    				Logger.loudLogLine(name + " pressed.");
                SoundManager.getManager().playSound(SoundManager.BUTTON_CLICK);
                renderClick();
                if(action != null)
                	action.activate();
            }
            super.mouseClicked(button, x, y, clickCount);
    	}
    	else
    		SoundManager.getManager().playSound(SoundManager.BUTTON_DISABLED);
    }

    private void renderClick(){
    	
    }
    
    public void setLocation(int X, int Y){
    	try{
	    	oldX = X;
            oldY = Y;
            bigX = X - (biggerFont.getWidth(text) - ttfont.getWidth(text))/2;
            bigY = Y - (biggerFont.getLineHeight() - ttfont.getLineHeight())/2;
    	}catch(Exception e){}
        super.setLocation(X, Y);
    }
    
}