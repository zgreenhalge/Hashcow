package SetUp;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class SimpleSlickGame extends BasicGame{
	
	private int X, xMult;
	private int Y, yMult;
	private boolean xDown, yDown;
	private static AppGameContainer appgc;
	private static Input input;

	public static void main(String[] args){
		try{
			appgc = new AppGameContainer(new SimpleSlickGame("This Is The Title"));
			appgc.setDisplayMode(800, 480, false);
			appgc.setShowFPS(true);
			appgc.start();
		}
		catch (SlickException ex){
			Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public SimpleSlickGame(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		//first time set up goes here
		input = gc.getInput();
		X = Y = 220;
		xDown = yDown = false;
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException{
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
			appgc.setShowFPS(!appgc.isShowingFPS());
		}
		////////////////////////////////////////////////////////////////
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			appgc.exit();
		}
		////////////////////////////////////////////////////////////////
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.drawString("Everything is working fine... for now", X, Y);
		g.drawString("X: " + X + " Y: " + Y, 650, 0);
		//g.drawLine(600, 0, 600, 480);
	}
}