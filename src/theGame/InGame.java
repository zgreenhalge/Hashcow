package theGame;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InGame extends BasicGameState {

	public static final int ID = 1;
	private static Random random;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		random = new Random();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ENTER)){
			container.getGraphics().setBackground(Color.white);
		}
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			game.enterState(MainMenu.ID);
		}

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
