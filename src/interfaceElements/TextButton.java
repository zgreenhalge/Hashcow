    package interfaceElements;
     
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
     
    public class TextButton extends MouseOverArea implements Button{
     
        private TrueTypeFont ttfont;
        private Font font;
        private String text;
        private boolean lastMouseOver = false;
        private StateBasedGame sbg;
        private int stateID;
        private boolean isEnabled = true;
        private TrueTypeFont biggerFont;
        private ButtonAction action;
        private boolean borderEnabled;
        private Color borderColor;
     
        public TextButton(GameContainer gc, Font font, String text, int x, int y, StateBasedGame sbg, int stateID, ButtonAction action) throws SlickException {
        	super((GUIContext)gc, new Image(0, 0), x, y, (new TrueTypeFont(font, false)).getWidth(text), (new TrueTypeFont(font, false)).getHeight());
        	this.ttfont = new TrueTypeFont(font, false);
            this.font = font;
            this.text = text;
            this.sbg = sbg;
            this.stateID = stateID;
            this.action = action;
            borderColor = Color.darkGray;
            biggerFont = new TrueTypeFont(new Font(font.getFontName(), font.BOLD, font.getSize()), false);
            //this.biggerFont = FontManager.getInstance().getSameFontWithSize(font, text, font.getFont().getSize() + 4);
        }
     
        public void setEnabled(boolean b) {
            isEnabled = b;
        }
     
        public boolean isEnabled() {
            return isEnabled;
        }
     
        public void setBorderEnabled(boolean b){
        	borderEnabled = b;
        }
        
        public boolean isBorderEnabled(){
        	return borderEnabled;
        }
        
        public void setAction(ButtonAction ba){
        	action = ba;
        }
        
        public ButtonAction getAction(){
        	return action;
        }
        
        public void setColor(Color c){
        	borderColor = c;
        }
        
        @Override
        public void render(GUIContext guic, Graphics g) {
            g.setFont(ttfont);
            Color standard = g.getColor();
            if (isEnabled) {
                g.setColor(Color.orange);
                if (isMouseOver()) {
                    g.setFont(biggerFont);
                    g.setColor(new Color(200, 50, 30));
                }
            } else {
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
            	Color prev = g.getColor();
            	g.setColor(borderColor);
            	g.drawLine(getX()-pad, getY()-pad, getX()+width+pad, getY()-pad);//top left to top right
            	g.drawLine(getX()-pad, getY()-pad, getX()-pad, getY()+height+pad);//top left to bottom left
            	g.drawLine(getX()-pad, getY()+height+pad, getX()+width+pad, getY()+height+pad);//bottom left to bottom right
            	g.drawLine(getX()+width+pad, getY()+height+pad, getX()+width+pad, getY()-pad);//bottom right to top right
            	g.setColor(prev);
            }
            g.drawString(text, getX(), getY());
            super.render(guic, g);
            g.setColor(standard);
        }
     
        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            if (sbg.getCurrentStateID() == stateID && isEnabled){//if in proper state & button is active
                if (isMouseOver() && !lastMouseOver){//if mouse is over && wasn't previously over
                    //SoundManager.getInstance().playSound(SoundManager.BUTTON_OVER); //play sound
                    lastMouseOver = true;	//make sure sound won't repeat
                } else if (!isMouseOver()) { //if mouse is not over button
                    lastMouseOver = false;	//allow sound to be played again
                }
            }
            super.mouseMoved(oldx, oldy, newx, newy);	//pass to super
        }
     
        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
        	if(isEnabled){
        		if (isMouseOver() && sbg.getCurrentStateID() == stateID) {
	                //SoundManager.getInstance().playSound(SoundManager.BUTTON_CLICK);
	            	action.activate();
	            }
	            super.mouseClicked(button, x, y, clickCount);
        	} else{
        		//SoundManager.getInstance().playSound(SoundManager.BUTTON_DISABLED)
        	}
        }
     
    }