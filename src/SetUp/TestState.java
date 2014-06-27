package SetUp;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import interfaceElements.Button;
import interfaceElements.TextButton;
import interfaceElements.buttonActions.ButtonAction;
import interfaceElements.buttonActions.ToggleFPSAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.SoundManager;
import utils.Logger;



public class TestState extends BasicGameState {

	private SoundManager sm;
	private Input input;
	private TextButton toggleFPSButton;
	private TextButton toggleBordersButton;
	private TextButton logExceptionButton;
	private TextButton logMessageButton;
	private ArrayList<Button> buttons;
	private Font buttonFont;
	private TrueTypeFont ttfont;
	private int Y;
	private int X;
	private boolean mouseWasDown;
	private int startX;
	private int startY;
	private int prevX;
	private int prevY;
	private boolean displayLog;
	private long displayDelta;

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException{
		//first time set up goes here
		try{
			Logger.init(new File("logs"), gc, true);
			Logger.setVerboseEnabled(true);
		}catch(IOException ioe){
			Logger.loudLog(ioe);
		}
		buttons = new ArrayList<Button>();
		mouseWasDown = false;
		buttonFont = new Font("Verdana", Font.PLAIN, 12);
		ttfont = new TrueTypeFont(buttonFont, false);
		sm = SoundManager.getManager();
		SoundManager.setVolume(1.0f);
		sm.loopSound(SoundManager.MAIN_MENU);
		toggleFPSButton = new TextButton(gc,
				buttonFont,
				"Toggle FPS",
				790-ttfont.getWidth("Toggle FPS"), 470-ttfont.getHeight(),
				game,
				this.getID(),
				new ToggleFPSAction(gc));
		toggleBordersButton = new TextButton(gc,
				buttonFont,
				"Toggle Borders",
				790-ttfont.getWidth("Toggle Borders"), 460-ttfont.getHeight()*2,
				game,
				this.getID(), 
				new ButtonAction(){
					public void activate(){
						for(Button b: buttons)
							if(b instanceof TextButton)
								((TextButton)b).setBorderEnabled(!((TextButton)b).isBorderEnabled());
					}
				});
		logMessageButton = new TextButton(gc,
				buttonFont,
				"Log Message",
				790-ttfont.getWidth("Log Message"), 450-ttfont.getHeight()*3,
				game,
				this.getID(), 
				new ButtonAction(){
					public void activate(){
						Logger.loudLogLine("Message Print Requested");
					}
				});
		logExceptionButton = new TextButton(gc,
				buttonFont,
				"Log Exception",
				790-ttfont.getWidth("Log Exception"), 440-ttfont.getHeight()*4,
				game,
				this.getID(), 
				new ButtonAction(){
					public void activate(){
						Logger.loudLog(new SlickException("Exception Print Request"));
					}
				});
		buttons.add(toggleBordersButton);
		buttons.add(toggleFPSButton);
		buttons.add(logMessageButton);
		buttons.add(logExceptionButton);
		input = gc.getInput();
		displayLog = false;
		X = Y = 0;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawString("Everything is working fine... for now", X+220, Y+220);
		g.drawString("X: " + X + " Y: " + Y, 650, 0);
		for(Button b: buttons)
			b.render((GUIContext)container, g);
		if(displayLog) Logger.render();		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		//actions every step go here
		displayDelta += delta;
		if(displayDelta > 10000)
			displayLog = false;
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
		if(input.isKeyPressed(Input.KEY_GRAVE)){
			displayLog = !displayLog;
			displayDelta = 0;
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
			SimpleSlickGame.exit();
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
