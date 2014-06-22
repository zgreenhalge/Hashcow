package SetUp;

import java.awt.Font;
import java.util.ArrayList;

import interfaceElements.Button;
import interfaceElements.TextButton;
import interfaceElements.ToggleBordersAction;
import interfaceElements.ToggleFPSAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.SoundManager;



public class TestState extends BasicGameState {

	private SoundManager sm;
	private Input input;
	private TextButton toggleFPSButton;
	private TextButton toggleBordersButton;
	private ArrayList<Button> buttons;
	private Font buttonFont;
	private int Y;
	private int X;
	private boolean mouseWasDown;
	private int startX;
	private int startY;
	private int prevX;
	private int prevY;

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException{
		//first time set up goes here
		buttons = new ArrayList<Button>();
		mouseWasDown = false;
		buttonFont = new Font("Verdana", Font.PLAIN, 12);
		sm = new SoundManager(gc);
		sm.setVolume(.25f);
		sm.loopSound(SoundManager.MAIN_MENU);
		toggleFPSButton = new TextButton(gc,
				buttonFont,
				"Toggle FPS",
				790-new TrueTypeFont(buttonFont, false).getWidth("Toggle FPS"), 470-new TrueTypeFont(buttonFont, false).getHeight(),
				game,
				this.getID(),
				new ToggleFPSAction(gc));
		toggleBordersButton = new TextButton(gc,
				buttonFont,
				"Toggle Borders",
				790-new TrueTypeFont(buttonFont, false).getWidth("Toggle Borders"), 460-new TrueTypeFont(buttonFont, false).getHeight()*2,
				game,
				this.getID(), 
				new ToggleBordersAction(buttons));
		buttons.add(toggleBordersButton);
		buttons.add(toggleFPSButton);
		input = gc.getInput();
		X = Y = 0;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawString("Everything is working fine... for now", X+220, Y+220);
		g.drawString("X: " + X + " Y: " + Y, 650, 0);
		toggleFPSButton.render((GUIContext)container, g);
		toggleBordersButton.render((GUIContext)container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		//actions every step go here
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			if(mouseWasDown){
				X = prevX + input.getMouseX() - startX;
				Y = prevY + input.getMouseY() - startY;
			}else{
				startX = input.getMouseX();
				startY = input.getMouseY();
				prevX = X;
				prevY = Y;
				mouseWasDown = true;
			}
		}else{
			mouseWasDown = false;
		}
		if(input.isKeyDown(Input.KEY_LEFT)){
			X -= 1;
		}
		if(input.isKeyDown(Input.KEY_RIGHT)){
			X += 1;
		}
		if(input.isKeyDown(Input.KEY_UP)){
			Y -= 1;
		}
		if(input.isKeyDown(Input.KEY_DOWN)){
			Y += 1;
		}
		if(input.isKeyDown(Input.KEY_SPACE)){
			X = Y = 0;
		}
		////////////////////////////////////////////////////////////////
		if(input.isKeyPressed(Input.KEY_TAB)){
			container.setShowFPS(!container.isShowingFPS());
		}
		////////////////////////////////////////////////////////////////
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			container.exit();
		}
		////////////////////////////////////////////////////////////////
	}

	@Override
	public int getID() {
		return 0;
	}

	public Button[] getButtons(){
		return (Button[])buttons.toArray();
	}
	
}
