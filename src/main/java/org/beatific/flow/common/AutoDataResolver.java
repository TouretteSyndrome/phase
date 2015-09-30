package org.beatific.flow.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.beatific.flow.repository.Copy;

public abstract class AutoDataResolver {

	public synchronized Object get(String key) {
		Class<?> clazz = this.getClass();
		try {
			Field field = clazz.getDeclaredField(key);
			field.setAccessible(true);
			return field.get(this);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		
		return null;
	}
	
	public synchronized void put(String key, Object value) {
		Class<?> clazz = this.getClass();
		
		try {
			Field field = clazz.getDeclaredField(key);
			field.setAccessible(true);
			field.set(this, value);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	public synchronized String[] fieldList() {
		
		Class<?> clazz = this.getClass();
		
		List<String> fields = new ArrayList<String>();
		
		for(Field field : clazz.getDeclaredFields()) {
			
			Copy copy = field.getAnnotation(Copy.class);
			
			if(copy == null) continue;
			
			fields.add(field.getName());
		}
		
		return fields.toArray(new String[0]);
	}
}
