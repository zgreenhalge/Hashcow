package guiElements;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

import actions.Action;
import actions.GenericAction;
import actions.WrapperAction;
import resourceManager.ImageManager;
import theGame.Main;
import utils.Logger;

public class ExpandingMenu extends MouseOverArea implements Menu{

	public static final int UP = 1;
	public static final int DOWN = 2;
	
	private static int lastId = 1;
	
	private String name;
	private ArrayList<Button> buttons;
	private int X;
	private int baseY;
	private int expandY;
	private int buttonHeight;
	private int MAX_BUTTONS;
	private int orientation;
	private boolean expanded;
	private Button base;
	private int targetState;
	private boolean hidden;
	private boolean reporting;
	
	private Rectangle baseFill;
	private Rectangle expandFill;
	private static SpriteSheet expandDown = ImageManager.getSpriteSheet("res/images/buttons/expandDownButton.png", 32, 32, 1);
	private static SpriteSheet expandUp = ImageManager.getSpriteSheet("res/images/buttons/expandUpButton.png", 32, 32, 1);
	private ImageButton expand;
	private Action expandAction = new GenericAction(){ public void activate(){expand();}};
	
	
	public ExpandingMenu(GameContainer container, int stateId, Button base, int baseX, int baseY, int orientation, int maxDisplay) throws SlickException{
		super(container, new Image(base.getWidth() + base.getHeight(), base.getHeight()), baseX, baseY);
		buttons = new ArrayList<Button>();
		MAX_BUTTONS = maxDisplay;
		X = baseX;
		base.setLocation(baseX, baseY);
		targetState = stateId;
		this.baseY = baseY;
		this.orientation = orientation;
		SpriteSheet use;
		if(orientation == UP)
			use = expandUp;
		else 
			use = expandDown;
		if(use == null)
			use = new SpriteSheet(new Image(base.getHeight(), base.getHeight()), base.getHeight(), base.getHeight());
		int height = base.getHeight();
		expand = new ImageButton(container,
				new SpriteSheet(use.getScaledCopy((height+1)*3, (height+1)*3), height, height, 1),
				baseX + base.getWidth(), baseY + base.getHeight(),
				Main.getGame(), Main.getGame().getCurrentStateID(),
				expandAction);
		this.base = base;
		base.setAction(new WrapperAction(base.getAction()){
			public void activate(){
				if(expanded)
					collapse();
				else
					expand();
				this.action.activate();
			}
		});
		baseFill = new Rectangle(baseX-5, baseY, super.getWidth()+10, super.getHeight());
		expandFill = new Rectangle(0, 0, 0, 0);
		name = base.getName() + "ExpandingMenu";
	}
	
	public void setX(int x){
		X = x;
		base.setLocation(X, baseY);
		baseFill.setX(x-5);
		buttonHeight = 0;
		if(orientation == UP){
			for(Button b: buttons){
				buttonHeight += b.getHeight();
				b.setLocation(X, baseY - buttonHeight);
			}
		}
		else{
			for(Button b: buttons){
				b.setLocation(X, baseY + getHeight() + buttonHeight);
				buttonHeight += b.getHeight();
			}
		}
		super.setX(x);
	}
	
	public void setY(int y){
		baseY = y;
		base.setLocation(X, baseY);
		baseFill.setY(y);
		buttonHeight = 0;
		if(orientation == UP){
			for(Button b: buttons){
				buttonHeight += b.getHeight();
				b.setLocation(X, baseY - buttonHeight);
			}
		}
		else{
			for(Button b: buttons){
				b.setLocation(X, baseY + getHeight() + buttonHeight);
				buttonHeight += b.getHeight();
			}
		}
		super.setY(y);
	}
	
	public void addButton(Button b){
		buttons.add(b);
		b.setAction(new WrapperAction(b.getAction()){
			public void activate(){
				action.activate();
				collapse();
			}
		});
		if(orientation == UP){
			buttonHeight += b.getHeight();
			b.setLocation(X, baseY - buttonHeight);
		}
		else{
			b.setLocation(X, baseY + getHeight() + buttonHeight);
			buttonHeight += b.getHeight();
		}
	}
	
	public void removeButton(Button b){
		buttons.remove(b);
		buttonHeight -= b.getHeight();
	}
	
	public void expand(){
		if(reporting)
			Logger.loudLogLine(name + " expanded");
		if(orientation == UP){
			expandY = baseY - 1;
			if(buttons.size() >= MAX_BUTTONS)
				for(int n=0; n< MAX_BUTTONS; n++)
					expandY -= buttons.get(n).getHeight();
			else
				for(Button b: buttons)
					expandY -= b.getHeight();
			expandFill = new Rectangle(X-5, expandY, getWidth()+10, Math.abs(baseY-expandY)+2);
		}else{
			int height = 0;
			if(buttons.size() >= MAX_BUTTONS)
				for(int n=0; n< MAX_BUTTONS; n++)
					height += buttons.get(n).getHeight();
			else
				for(Button b: buttons)
					height += b.getHeight();
			expandFill = new Rectangle(X-5, baseY+getHeight()-1, getWidth()+10, height+1);
		}
		for(Button b: buttons)
			if(expandFill.contains(b.getX()+b.getWidth()/2, b.getY()+b.getHeight()/2)){
				b.setHidden(false);
			}else{
				b.setHidden(true);
			}
		expanded = true;
	}
	
	public void collapse(){
		if(reporting)
			Logger.loudLogLine(name + " collapsed");
		for(Button b: buttons){
			b.setHidden(true);
		}
		expanded = false;
	}
	
	public void setReporting(boolean b){
		base.setReport(b);
		for(Button button: buttons)
			button.setReport(b);
	}

	public void setHidden(boolean b) {
		hidden = b;
		base.setHidden(b);
		for(Button button: buttons)
			button.setHidden(b);
	}
	
	public boolean isHidden(){
		return hidden;
	}

	@Override
	public boolean contains(int x, int y) {
		if(expanded)
			return expandFill.contains(x, y) || baseFill.contains(x, y);
		return baseFill.contains(x, y);
	}

	@Override
	public boolean mouseClick(int button, int x, int y){
		if(hidden || Main.getGame().getCurrentStateID() != targetState)
			return false;
		if(base.mouseClick(button, x, y))
			return true;
		for(Button b: buttons)
			if(b.mouseClick(button, x, y))
				return true;
		if(expanded && expandFill.contains(x, y))
			return true;	
		collapse();
		return false;
	}

	@Override
	public boolean mouseMove(int oldx, int oldy, int newx, int newy) {
		if(base.mouseMove(oldx, oldy, newx, newy) || baseFill.contains(newx, newy))
			return true;
		for(Button b: buttons)
			if(b.mouseMove(oldx, oldy, newx, newy))
				return true;
		if(expanded)
			if(expandFill.contains(newx, newy))
				return true;
		return false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(hidden)
			return;
		Color prev = g.getColor();
		if(expanded){
			g.setColor(Color.lightGray);
			g.fill(expandFill);
			for(Button b: buttons)
				b.render(container, game, g);
		}
		g.setColor(Color.lightGray);
		g.fill(baseFill);
		base.render(container, game, g);
		expand.render(container, game, g);
		g.setColor(prev);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		for(Button b: buttons)
			b.update(container, game, delta);
	}

	@Override
	public void setName(String s) {
		name = s;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isReporting() {
		return reporting;
	}

	@Override
	public void setReport(boolean b) {
		reporting = b;
	}

	@Override
	public boolean mouseWheelMove(int change) {
		if(hidden)
			return false;
		if(Main.getGame().getCurrentStateID() == targetState){
			Input in = Main.getStaticContainer().getInput();
			if(contains(in.getMouseX(), in.getMouseY())){
				if(buttons.size() > MAX_BUTTONS){
					for(Button b: buttons){
						if(change > 0)
							b.setLocation(b.getX(), b.getY()-4);
						else
							b.setLocation(b.getX(), b.getY()+4);
						if(expandFill.contains(b.getX(), b.getY()))
							b.setHidden(false);
						else
							b.setHidden(true);
					}
				return true;
				}
			}
		}
		return false;		
	}
	
}
