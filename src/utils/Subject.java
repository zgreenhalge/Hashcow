package utils;

public interface Subject {

	public void registerObserver(Observer obs);
	public void unregisterObserver(Observer obs);
	public void alertObservers();
	public int getID();
	
}
