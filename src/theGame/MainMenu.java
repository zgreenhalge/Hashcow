package theGame;


import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import utils.Logger;

public class MainMenu extends HCGameState {

	public static final int ID = 001;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		try{Logger.init(new File("logs"), container);}
		catch(IOException e){
			e.printStackTrace();
		}
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		Font awtFont = new Font("Verdana", Font.BOLD, 24);
	    g.setFont(new TrueTypeFont(awtFont, false));
	    g.drawString("THERE IS NO MENU", 275, 200);
	    super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			container.exit();
		if(container.getInput().isKeyPressed(Input.KEY_ENTER))
			game.enterState(InGame.ID);
		super.update(container, game, delta);
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
