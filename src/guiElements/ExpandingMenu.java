package guiElements;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import actions.Action;
import actions.GenericAction;
import actions.WrapperAction;
import resourceManager.ImageManager;
import theGame.Main;

public class ExpandingMenu extends MouseOverArea{

	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	
	private ArrayList<Button> buttons;
	private int X;
	private int width;
	private int baseY;
	private int expandY;
	private int baseHeight;
	private int buttonHeight;
	private int MAX_HEIGHT;
	private int orientation;
	private boolean expanded;
	private Button base;
	
	private static Rectangle baseFill;
	private static Rectangle expandFill;
	private static SpriteSheet expandDown = ImageManager.getSpriteSheet("res/images/buttons/expandDownButton.png", 32, 32, 1);
	private static SpriteSheet expandUp = ImageManager.getSpriteSheet("res/images/buttons/expandUpButton.png", 32, 32, 1);
	private ImageButton expand;
	private Action expandAction = new GenericAction(){ public void activate(){expand();}};
	
	
	public ExpandingMenu(GameContainer container, Button base, int baseX, int baseY, int orientation, int expandedHeight) throws SlickException{
		super(container, new Image(base.getWidth() + base.getHeight(), base.getHeight()), baseX, baseY);
		buttons = new ArrayList<Button>();
		MAX_HEIGHT = expandedHeight;
		X = baseX;
		base.setLocation(baseX, baseY);
		this.baseY = baseY;
		baseHeight = base.getHeight();
		width = base.getWidth() + base.getHeight();
		this.orientation = orientation;
		SpriteSheet use;
		if(orientation == TOP)
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
				expand();
				this.action.activate();
			}
		});
		baseFill = new Rectangle(baseX-5, baseY, super.getWidth()+10, super.getHeight());
	}
	
	@Override
    public void render(GUIContext guic, Graphics g){
		Color prev = g.getColor();
		if(expanded){
			g.setColor(Color.lightGray);
			g.fill(expandFill);
			g.setColor(Color.darkGray);
			g.draw(expandFill);
			for(Button b: buttons)
				b.render(guic, g);
		}
		g.setColor(Color.lightGray);
		g.fill(baseFill);
		g.setColor(Color.darkGray);
		g.draw(baseFill);
		base.render(guic, g);
		expand.render(guic, g);
		g.setColor(prev);
	}
	
	public void addButton(Button b){
		buttons.add(b);
		buttonHeight += b.getHeight();
		if(orientation == TOP)
			b.setLocation(X, baseY - buttonHeight);
		else
			b.setLocation(X, baseY + buttonHeight);
	}
	
	public void removeButton(Button b){
		buttons.remove(b);
		buttonHeight -= b.getHeight();
	}
	
	public void expand(){
		Button temp;
		if(orientation == TOP){
			if(buttonHeight >= MAX_HEIGHT)
				expandY = baseY - MAX_HEIGHT;
			else
				expandY = baseY - buttonHeight;
			expandFill = new Rectangle(X-5, expandY, width+10, baseY-expandY+5);
			int fill = baseY;
			for(int i=0; i<buttons.size(); i++){
				temp = buttons.get(i);
				fill -= temp.getHeight();
				if(fill > expandY){
					temp.setLocation(X, fill);
					temp.setHidden(false);
				}else
					temp.setHidden(true);
			}
		}else{
			if(buttonHeight >= MAX_HEIGHT)
				expandY = baseY + MAX_HEIGHT;
			else
				expandY = baseY + buttonHeight;
			expandFill = new Rectangle(X-5, baseY+baseHeight, width+10, expandY-baseY+5);
			int fill = baseY+baseHeight;
			for(int i=0; i< buttons.size(); i++){
				temp = buttons.get(i);
				fill += temp.getHeight();
				if(fill < expandFill.getY() + expandFill.getHeight()){
					temp.setLocation(X, fill);
					temp.setHidden(false);
				}else
					temp.setHidden(true);
			}
		}
		expanded = true;
	}
	
	public void collapse(){
		for(Button b: buttons)
			b.setHidden(true);
		expanded = false;
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if(expanded && isMouseOver())
			return;
		collapse();
	}
	
	@Override
	public void mouseWheelMoved(int change){
		if(buttonHeight > MAX_HEIGHT)
				for(Button b: buttons){
					if(change > 0)
						b.setLocation(b.getX(), b.getY()-4);
					else
						b.setLocation(b.getX(), b.getY()+4);
					if(expandFill.contains(b.getX(), b.getY()))
						continue;
					b.setHidden(true);
				}
	}
	
}
