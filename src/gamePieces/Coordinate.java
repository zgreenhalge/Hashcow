package gamePieces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A representation of a location given in X and Y values.
 *
 */
public class Coordinate implements Serializable{

	private static final long serialVersionUID = 559990832024871296L;
	
	private int X;
	private int Y;
	
	public Coordinate(int X, int Y){
		this.X = X;
		this.Y = Y;
	}
	
	/**
	 * Get the X value of this Coordinate
	 * @return the X value of the Coordinate
	 */
	public int X(){
		return X;
	}
	
	/**
	 * Get the Y value of the Coordinate
	 * @return the Y value of the Coordinate
	 */
	public int Y(){
		return Y;
	}
	
	/**
	 * Shift the X value n units. Specifically, adds n to the X value.
	 * @param n - the change in value of X
	 * @return the previous value of X before the n-shift
	 */
	public int X(int n){
		X += n;
		return X - n;
	}
	
	/**
	 * Shift the Y value n units. Specifically, adds n to the Y value.
	 * @param n - the change in value of Y
	 * @return the previous value of Y before the n-shift
	 */
	public int Y(int n){
		Y += n;
		return Y - n;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Coordinate))
			return false;
		Coordinate temp = (Coordinate) obj;
		return temp.X == this.X && temp.Y == this.Y;
	}
	
	@Override
	public int hashCode(){
		return X ^ Y;
	}
	
	@Override
	public String toString(){
		return X + "," + Y;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		ois.defaultReadObject();
	}

	/**
	 * Get a Coordinate that shares the values of the passed Coordinate
	 * @param target - the Coordinate to make a copy of
	 * @return a new Coordinate with the sme values as the one passed
	 */
	public static Coordinate copy(Coordinate target) {
		return new Coordinate(target.X, target.Y);
	}
	
}
