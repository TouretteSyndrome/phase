package org.beatific.flow.repository.support;

import java.util.HashMap;
import java.util.Map;

import org.beatific.flow.repository.Repository;

public abstract class OneStateRepository implements Repository<OneState>{

	protected IdLocal<Map<String, Object>> idStore = new IdLocal<Map<String, Object>>() {
		
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
	protected Map<String, Object> dataMap(Object object) {
		return idStore.get(object);
	}
	
	public OneState getState() {
		return OneState.ONE;
	}

	public abstract void save(Object object);
	
	public void save(Object state, Object object) {
		save(object);
	}

	public abstract Object load(Object object);
	
	public Object load(Object state, Object object) {
		return load(object);
	}

	public abstract void change(Object object);
	
	public void change(Object state, Object object) {
		change(object);
	}

	public abstract void remove(Object object);
	
	public void remove(Object state, Object object) {
		remove(object);
	}

}
