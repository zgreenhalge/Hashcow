package interfaceElements;
     
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;
     

import resourceManager.SoundManager;
import utils.Logger;
     
    public class ImageButton extends MouseOverArea implements Button {
     
        private boolean enabled = false;
        private boolean lastMouseOver = false;
        private StateBasedGame sbg;
        private int stateID;
        private ButtonAction action;
        private String name;
        private static int num = 0;
     
        /**
         * 
         * @param guic - the GameContainer
         * @param animation - Animation consisting of three states: 1-the normal button image, 2-the mouse over image, 3-the button clicked image
         * @param x - x value of top left
         * @param y - y value of top left
         * @param sbg - the current game
         * @param stateID - the state in which this button exists
         * @param action - the action the button will take when pressed
         * @throws SlickException - when something goes wrong
         */
        public ImageButton(GameContainer gc, Animation animation, int x, int y,
                StateBasedGame sbg, int stateID, ButtonAction action) throws SlickException {
            super((GUIContext)gc, animation.getImage(0), x, y);
            super.setMouseDownColor(Color.red);
            super.setMouseOverColor(Color.blue);
            super.setMouseOverImage(animation.getImage(1));
            super.setMouseDownImage(animation.getImage(2));
            this.sbg = sbg;
            this.stateID = stateID;
            this.action = action;
            name = "State"+stateID+"ImageButton"+(num++);
            
        }
     
        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            if (sbg.getCurrentStateID() == stateID) {
                if (isMouseOver() && !lastMouseOver && !isEnabled()) {
                    //SoundManager.getInstance().playSound(SoundManager.BUTTON_OVER);
                    lastMouseOver = true;
                } else if (!isMouseOver()) {
                    lastMouseOver = false;
                }
            }
            super.mouseMoved(oldx, oldy, newx, newy);
        }
     
        public boolean isEnabled() {
            return enabled;
        }
     
        public void setEnabled(boolean b) {
            enabled = b;
        }
     
        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
        	if(enabled){
	            if (isMouseOver() && sbg.getCurrentStateID() == stateID) {
	                //SoundManager.getInstance().playSound(SoundManager.BUTTON_CLICKED);
	            	action.activate();
	            	Logger.logLine(name + " pressed.");
	            }
	            super.mouseClicked(button, x, y, clickCount);
        	}
        	else{
        		//SoundManager.getInstance().playSound(SoundManager.BUTTON_DISABLED);
        	}
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
     
    }