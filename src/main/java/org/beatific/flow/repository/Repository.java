package org.beatific.flow.repository;

public interface Repository<S extends Enum<?>> {

	public S getState();
	
	public void save(Object state, Object object);
	
	public Object load(Object state, Object object);
	
	public void change(Object state, Object object);
	
	public void remove(Object state, Object object);
}
