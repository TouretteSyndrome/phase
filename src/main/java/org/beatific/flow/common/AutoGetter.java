package org.beatific.flow.common;

import java.lang.reflect.Field;

public abstract class AutoGetter {

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
}
