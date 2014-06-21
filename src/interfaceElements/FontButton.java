    package interfaceElements;
     
    import org.newdawn.slick.Color;
    import org.newdawn.slick.Graphics;
    import org.newdawn.slick.Image;
    import org.newdawn.slick.SlickException;
    import org.newdawn.slick.UnicodeFont;
    import org.newdawn.slick.gui.GUIContext;
    import org.newdawn.slick.gui.MouseOverArea;
    import org.newdawn.slick.state.StateBasedGame;
     
    public class FontButton extends MouseOverArea {
     
        private UnicodeFont font;
        private String text;
        private boolean lastMouseOver = false;
        private StateBasedGame sbg;
        private int stateID;
        private boolean isEnabled = true;
        private UnicodeFont biggerFont;
     
        public FontButton(GUIContext guic, UnicodeFont font, String text, int x,
                int y, int width, int height, StateBasedGame sbg, int stateID)
                throws SlickException {
            super(guic, new Image(0, 0), x, y, width, height);
            this.font = font;
            this.text = text;
            this.sbg = sbg;
            this.stateID = stateID;
            //this.biggerFont = new UnicodeFont();
            //this.biggerFont = FontManager.getInstance().getSameFontWithSize(font,
                    //text, font.getFont().getSize() + 4);
        }
     
        public void setIsEnabled(boolean b) {
            isEnabled = b;
        }
     
        public boolean isEnabled() {
            return isEnabled;
        }
     
        @Override
        public void render(GUIContext guic, Graphics g) {
            g.setFont(font);
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
        }
     
        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            if (sbg.getCurrentStateID() == stateID && isEnabled) {
                if (isMouseOver() && !lastMouseOver) {
                    //SoundManager.getInstance().getButtonOver().play(1, (float) .2);
                    lastMouseOver = true;
                } else if (!isMouseOver()) {
                    lastMouseOver = false;
                }
            }
            super.mouseMoved(oldx, oldy, newx, newy);
        }
     
        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
            if (isMouseOver() && sbg.getCurrentStateID() == stateID && isEnabled) {
                //SoundManager.getInstance().getButtonClick().play();
            }
            super.mouseClicked(button, x, y, clickCount);
        }
     
    }