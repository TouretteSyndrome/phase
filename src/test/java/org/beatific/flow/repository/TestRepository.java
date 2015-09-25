package org.beatific.flow.repository;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Store(id="test")
public class TestRepository extends OneStateRepository{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ThreadLocal<Map<String, Object>> threadStore = new ThreadLocal<Map<String, Object>>() {
		
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
	private Map<String, Object> dataMap() {
		return threadStore.get();
	}
	
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
