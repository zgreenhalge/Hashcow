package gamePieces;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

public class PlayerColors implements Iterable<Color>{

	public static final int BLACK = 0;
	public static final int BLUE = 1;
	public static final int CYAN = 2;
	public static final int GRAY = 3;
	public static final int GREEN = 4;
	public static final int MAGENTA = 5;
	public static final int ORANGE = 6;
	public static final int PINK = 7;
	public static final int RED = 8;
	public static final int WHITE = 9;
	public static final int YELLOW = 10;
	
	private Color black;
	private Color blue;
	private Color cyan;
	private Color gray;
	private Color green;
	private Color magenta;
	private Color orange;
	private Color pink;
	private Color red;
	private Color white;
	private Color yellow;
	
	private static ArrayList<Color> colors;
	
	public PlayerColors(){
		colors = new ArrayList<Color>();
		colors.add(black = Color.black);
		colors.add(blue = Color.blue);
		colors.add(cyan = Color.cyan);
		colors.add(gray = Color.gray);
		colors.add(green = Color.green);
		colors.add(magenta = Color.magenta);
		colors.add(orange = Color.orange);
		colors.add(pink = Color.pink);
		colors.add(red = Color.red);
		colors.add(white = Color.white);
		colors.add(yellow = Color.yellow);
	}
	
	public Color getColor(int id){
		switch(id){
			case BLACK: return black;
			case BLUE: return blue;
			case CYAN: return cyan;
			case GRAY: return gray;
			case GREEN: return green;
			case MAGENTA: return magenta;
			case ORANGE: return orange;
			case PINK: return pink;
			case RED: return red;
			case WHITE: return white;
			case YELLOW: return yellow;
			default: return Color.transparent;
		}
	}
	
	public int getId(Color c){
		return colors.indexOf(c);
	}
	
	public int getNumColors(){
		return colors.size();
	}
	
	public Image getColorAsImage(Color color, int width, int height){
		ImageBuffer buffer = new ImageBuffer(width, height);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int a = color.getAlpha();
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++)
				buffer.setRGBA(x, y, r, g, b, a);
		return buffer.getImage();
	}
	
	@Override
	public Iterator<Color> iterator() {
		return colors.iterator();
	}
	
	
}
