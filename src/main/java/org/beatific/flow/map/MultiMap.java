package org.beatific.flow.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * multimap is map that can put one key to multi value.
 * 
 * @author P067880
 */

public class MultiMap<K,V> {

	private Map<K, List<V>> relations = new HashMap<K, List<V>>();
	private List<V> values = new ArrayList<V>();
	
	public int size() {
		return relations.size();
	}

	public boolean isEmpty() {
		return relations.isEmpty();
	}

	public boolean containsKey(Object key) {
		return relations.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return values.contains(value);
	}
	
	public boolean contains(Object key, Object value) {
		
		if(relations.containsKey(key) && relations.get(key).contains(value)) return true;
		return false;
	}

	public List<V> get(Object key) {
		return relations.get(key);
	}
	
	private List<V> getInstance(K key) {
		List<V> list = relations.get(key);
		if(list == null){
			list = new ArrayList<V>();
			relations.put(key, list);
		}
		return list;
	}
	
	public void put(K key, V value) {
		Assert.notNull(key);
		Assert.notNull(value);
		
		if(contains(key, value))return;
			
		List<V> list = getInstance(key);
		list.add(value);
		values.add(value);
	}

	public List<V> remove(Object key) {
		
		List<V> list = relations.remove(key);
		if(list == null)return list;
		for(V value : list) {
			values.remove(value);
		}
		
		return list;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		Set<? extends K> keySet = m.keySet();
		for(K key : keySet) {
			put(key, m.get(key));
		}
	}

	public void clear() {
		relations.clear();
		values.clear();
	}

	public Set<K> keySet() {
		return relations.keySet();
	}

	public Collection<V> values() {
		return values;
	}

	@Override
	public String toString() {
		return "OneToNMap [" + relations + "]";
	}
	
	
}
