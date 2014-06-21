    package interfaceElements;
     
    import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;
     
    public class TextButton extends MouseOverArea {
     
        private TrueTypeFont font;
        private String text;
        private boolean lastMouseOver = false;
        private StateBasedGame sbg;
        private int stateID;
        private boolean isEnabled = true;
        private TrueTypeFont biggerFont;
        private ButtonAction action;
        private Color standard;
     
        public TextButton(GameContainer gc, TrueTypeFont font, String text, int x, int y, StateBasedGame sbg, int stateID, ButtonAction action) throws SlickException {
            super((GUIContext)gc, new Image(0, 0), x, y, font.getWidth(text), font.getHeight());
            this.font = font;
            this.text = text;
            this.sbg = sbg;
            this.stateID = stateID;
            this.action = action;
            
            this.biggerFont = font;
            //this.biggerFont = FontManager.getInstance().getSameFontWithSize(font,
                    //text, font.getFont().getSize() + 4);
        }
     
        public void setEnabled(boolean b) {
            isEnabled = b;
        }
     
        public boolean isEnabled() {
            return isEnabled;
        }
     
        @Override
        public void render(GUIContext guic, Graphics g) {
            g.setFont(font);
            standard = g.getColor();
            if (isEnabled) {
                g.setColor(Color.orange);
                if (isMouseOver()) {
                    g.setFont(biggerFont);
                    g.setColor(new Color(200, 50, 30));
                }
            } else {
                g.setColor(Color.gray);
            }
            g.drawString(text, getX(), getY());
            super.render(guic, g);
            g.setColor(standard);
        }
     
        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            if (sbg.getCurrentStateID() == stateID && isEnabled){//if in proper state & button is active
                if (isMouseOver() && !lastMouseOver){//if mouse is over && wasn't previously over
                    //SoundManager.getButtonOver().play(); //play sound
                    lastMouseOver = true;	//make sure sound won't repeat
                } else if (!isMouseOver()) { //if mouse is not over button
                    lastMouseOver = false;	//allow sound to be played again
                }
            }
            super.mouseMoved(oldx, oldy, newx, newy);	//pass to super
        }
     
        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
            if (isMouseOver() && sbg.getCurrentStateID() == stateID && isEnabled) {
                //SoundManager.getInstance().getButtonClick().play();
            	action.activate();
            }
            super.mouseClicked(button, x, y, clickCount);
        }
     
    }