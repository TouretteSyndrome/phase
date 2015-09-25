package org.beatific.flow.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Store(id="test")
public class TestRepository extends OneStateRepository{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void save(Object object) {
		
		dataMap().put(object.getClass().getName(), object);
	}

	@Override
	public Object load(Object object) {
		
		return dataMap();
	}

	@Override
	public void change(Object object) {
		
	}

	@Override
	public void remove(Object object) {
		dataMap().clear();
	}

}
