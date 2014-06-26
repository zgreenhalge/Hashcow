package theGame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.UnitImage;
import resourceManager.UnitImageLibrary;

public class InGame extends HCGameState {

	public static final int ID = 002;
	
	private UnitImage mechLight;
	private Animation sprite;
	private int Y;
	private int X;
	private boolean mouseWasDown;
	private int startX;
	private int startY;
	private int prevX;
	private int prevY;
	
	private Input input;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException{
		UnitImageLibrary lib = new UnitImageLibrary();
		mechLight = lib.getUnitImage(UnitImageLibrary.MECH_LIGHT);
		sprite = mechLight.getAnimation(UnitImage.IDLE);
		sprite.start();
		mouseWasDown = false;
		
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setDrawMode(Graphics.MODE_NORMAL);
		sprite.draw(X+container.getWidth()/2, Y+container.getHeight()/2);
		g.drawString("X: " + (X+container.getWidth()/2) + "  Y: " + (Y+container.getHeight()/2), 650, 10);
		// TODO Auto-generated method stub
		super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		input = container.getInput();
		sprite.update(delta);
		if(input.isKeyPressed(Input.KEY_ENTER)){
			container.getGraphics().setBackground(Color.white);
		}
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			game.enterState(MainMenu.ID);
		}
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
			sprite = mechLight.getAnimation(UnitImage.LEFT);
		}
		if(input.isKeyDown(Input.KEY_RIGHT)){
			X += 1;
			sprite = mechLight.getAnimation(UnitImage.RIGHT);
		}
		if(input.isKeyDown(Input.KEY_UP)){
			Y -= 1;
			sprite = mechLight.getAnimation(UnitImage.UP);
		}
		if(input.isKeyDown(Input.KEY_DOWN)){
			Y += 1;
			sprite = mechLight.getAnimation(UnitImage.DOWN);
		}
		if(input.isKeyDown(Input.KEY_SPACE)){
			X = Y = 0;
			sprite = mechLight.getAnimation(UnitImage.IDLE);
		}
		super.update(container, game, delta);

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
