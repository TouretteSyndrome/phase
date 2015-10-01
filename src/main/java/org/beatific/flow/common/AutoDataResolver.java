package org.beatific.flow.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.beatific.flow.repository.support.CopyFrom;
import org.beatific.flow.repository.support.CopyTo;

public abstract class AutoDataResolver {

	public synchronized Object get(String key) {
		
		return get(this, this.getClass(), key);
	}
	
	private Object get(Object object, Class<?> clazz, String key) {
		
		try {
			Field field = clazz.getDeclaredField(key);
			field.setAccessible(true);
			return field.get(object);
		} catch (NoSuchFieldException e) {
			if(clazz.getSuperclass() != null) get(object, clazz.getSuperclass(), key);
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		
		return null;
	}
	
	public synchronized void put(String key, Object value) {
		
		put(this, this.getClass(), key, value);
	}
	
	private void put(Object object, Class<?> clazz, String key, Object value) {
		try {
			Field field = clazz.getDeclaredField(key);
			field.setAccessible(true);
			field.set(object, value);
		} catch (NoSuchFieldException e) {
			if(clazz.getSuperclass() != null) put(object, clazz.getSuperclass(), key, value);
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	public synchronized String[] fieldListForCopyFrom() {
		
		return fieldListForCopyFrom(new ArrayList<String>(), this.getClass());
	}
	
    private String[] fieldListForCopyFrom(List<String> fields, Class<?> clazz) {
		
		for(Field field : clazz.getDeclaredFields()) {
			
			CopyFrom copy = field.getAnnotation(CopyFrom.class);
			
			if(copy == null) continue;
			
			fields.add(field.getName());
		}
		
		if(clazz.getSuperclass() == null) return fields.toArray(new String[0]);
		
		return fieldListForCopyFrom(fields, clazz.getSuperclass());
	}
    
public synchronized String[] fieldListForCopyTo() {
		
		return fieldListForCopyTo(new ArrayList<String>(), this.getClass());
	}
	
    private String[] fieldListForCopyTo(List<String> fields, Class<?> clazz) {
		
		for(Field field : clazz.getDeclaredFields()) {
			
			CopyTo copy = field.getAnnotation(CopyTo.class);
			
			if(copy == null) continue;
			
			fields.add(field.getName());
		}
		
		if(clazz.getSuperclass() == null) return fields.toArray(new String[0]);
		
		return fieldListForCopyTo(fields, clazz.getSuperclass());
	}
}
