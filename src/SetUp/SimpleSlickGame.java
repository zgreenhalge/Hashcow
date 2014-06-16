package SetUp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SimpleSlickGame extends BasicGame
{
	public SimpleSlickGame(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException{
		//first time set up goes here
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException{
		//actions every step go here
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.drawString("Everything is working fine... for now", 10, 10);
		//g.drawLine(600, 0, 600, 480);
	}

	public static void main(String[] args){
		try{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new SimpleSlickGame("This Is The Title"));
			appgc.setDisplayMode(800, 480, false);
			appgc.setShowFPS(false);
			appgc.start();
		}
		catch (SlickException ex){
			Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}