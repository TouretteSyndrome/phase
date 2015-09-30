package org.beatific.flow.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class IdLocal<T> {
	
	private Map<String, T> localMap = new HashMap<String, T>();

	private T get(String id) {
		return localMap.get(id);
	}
	
	public T get(Object object) {
		
		T t = get(id(object));
		
        if (t != null) {
        	
            return (T)t;
        }
        
        return setInitialValue(object);
    }
	
	private T setInitialValue(Object object) {
        T value = initialValue();
       	localMap.put(id(object), value);
        
        return value;
    }
	
	protected abstract T initialValue();
	
	private String id(Object object) {
		
		Class<?> clazz = object.getClass();
		for(Annotation annotation : clazz.getAnnotations()) {
			Class<?> annotationClass = annotation.getClass();
			try {
				Method id = annotationClass.getMethod("id", new Class[0]);
				return (String)id.invoke(annotation, new Object[0]);
			} catch (NoSuchMethodException e) {
			} catch (SecurityException e) {
			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
			}
		}
		
		return "default";
	}
}
