    package interfaceElements;
     
    import org.newdawn.slick.Animation;
    import org.newdawn.slick.Color;
    import org.newdawn.slick.Graphics;
    import org.newdawn.slick.Image;
    import org.newdawn.slick.SlickException;
    import org.newdawn.slick.gui.GUIContext;
    import org.newdawn.slick.gui.MouseOverArea;
    import org.newdawn.slick.state.StateBasedGame;
     
    import resourcemanager.SoundManager;
     
    public class ImageButton extends MouseOverArea {
     
        private boolean activated = false;
        private boolean lastMouseOver = false;
        private Animation animation;
        private Image inactiveButton;
        private Image activeButton;
        private StateBasedGame sbg;
        private int stateID;
     
        public ImageButton(GUIContext guic, Animation animation, int x, int y,
                StateBasedGame sbg, int stateID) throws SlickException {
            super(guic, animation.getImage(1), x, y);
            super.setMouseDownColor(Color.red);
            super.setMouseOverColor(Color.blue);
            this.animation = animation;
            this.sbg = sbg;
            this.stateID = stateID;
     
            inactiveButton = new Image("sprites/menu/button.png");
            activeButton = new Image("sprites/menu/button2.png");
        }
     
        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            if (sbg.getCurrentStateID() == stateID) {
                if (isMouseOver() && !lastMouseOver && !isActivated()) {
                    SoundManager.getInstance().getButtonOver().play(1, (float) .2);
                    lastMouseOver = true;
                } else if (!isMouseOver()) {
                    lastMouseOver = false;
                }
            }
            super.mouseMoved(oldx, oldy, newx, newy);
        }
     
        @Override
        public void render(GUIContext guic, Graphics g) {
            if (activated) {
                g.drawImage(activeButton, getX() - 7, getY() - 5);
                g.drawAnimation(animation, getX() + 2, getY() + 2);
            } else {
                g.drawImage(inactiveButton, getX() - 7, getY() - 5);
                super.render(guic, g);
            }
        }
     
        public boolean isActivated() {
            return activated;
        }
     
        protected void setActivated(boolean b) {
            activated = b;
        }
     
        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
            if (isMouseOver() && sbg.getCurrentStateID() == stateID) {
                activated = !activated;
                SoundManager.getInstance().getButtonClick().play();
            }
            super.mouseClicked(button, x, y, clickCount);
        }
     
    }