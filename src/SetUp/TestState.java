package SetUp;

import java.awt.Font;

import interfaceElements.TextButton;
import interfaceElements.ToggleFPSAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;



public class TestState extends BasicGameState {

	private Input input;
	private TextButton toggleFPSButton;
	private TrueTypeFont buttonFont;
	private int Y;
	private int X;

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException{
		//first time set up goes here
		buttonFont = new TrueTypeFont(new Font("Verdana", Font.PLAIN, 12), false);
		toggleFPSButton = new TextButton(gc,
				buttonFont,
				"Toggle FPS",
				790-buttonFont.getWidth("Toggle FPS"), 470-buttonFont.getHeight(),
				game,
				this.getID(),
				new ToggleFPSAction(gc));
		input = gc.getInput();
		X = Y = 0;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawString("Everything is working fine... for now", X+220, Y+220);
		g.drawString("X: " + X + " Y: " + Y, 650, 0);
		toggleFPSButton.render((GUIContext)container, g);
		//g.drawLine(600, 0, 600, 480);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		//actions every step go here
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
			X = Y = 220;
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

	
	
}
