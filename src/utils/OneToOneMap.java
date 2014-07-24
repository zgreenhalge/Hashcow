package utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class OneToOneMap<K, V> implements Serializable{

	private static final long serialVersionUID = 1346189071951100991L;
	
	private HashMap<K, V> keyToVal;
	private HashMap<V, K> valToKey;

	public OneToOneMap(){
		keyToVal = new HashMap<K, V>();
		valToKey = new HashMap<V, K>();
	}
	
	public boolean add(K key, V value){
		if(keyToVal.containsKey(key) || valToKey.containsKey(value))
			return false;
		keyToVal.put(key, value);
		valToKey.put(value, key);
		return true;
	}
	
	public boolean containsKey(K key){
		return keyToVal.containsKey(key);
	}
	
	public boolean containsValue(V value){
		return valToKey.containsKey(value);
	}
	
	public void put(K key, V value){
		keyToVal.put(key, value);
		valToKey.put(value, key);
	}
	
	public V getValue(K key){
		return keyToVal.get(key);
	}
	
	public K getKey(V value){
		return valToKey.get(value);
	}
	
	public boolean remove(K key){
		V temp = keyToVal.remove(key);
		if(temp != null)
			valToKey.remove(temp);
		return (temp == null);
	}
	
	public Set<Entry<K, V>> entrySet(){
		return keyToVal.entrySet();
	}
	
	public Set<K> keySet(){
		return keyToVal.keySet();
	}
	
	public Collection<V> values(){
		return keyToVal.values();
	}
	
	public void clear(){
		keyToVal.clear();
		valToKey.clear();
	}
	
}
