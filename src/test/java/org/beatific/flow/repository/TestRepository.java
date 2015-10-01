package org.beatific.flow.repository;

import org.beatific.flow.repository.support.OneStateRepository;


@Store(id="test")
public class TestRepository extends OneStateRepository{

	@Override
	public void save(Object object) {
		
		dataMap(object).put(object.getClass().getName(), object);
	}

	@Override
	public Object load(Object object) {
		return dataMap(object);
	}

	@Override
	public void change(Object object) {
		
	}

	@Override
	public void remove(Object object) {
		dataMap(object).clear();
	}

}
