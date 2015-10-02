package org.beatific.flow.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

	public static String[] findFieldNameForAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
		
		List<String> fieldNames = new ArrayList<String>();
		
		return findFieldNameForAnnotation(fieldNames, clazz, annotationType);
	}
	
	private static String[] findFieldNameForAnnotation(List<String> fields, Class<?> clazz, Class<? extends Annotation> annotationType) {

		for (Field field : clazz.getDeclaredFields()) {

			Annotation annotation = field.getAnnotation(annotationType);

			if (annotation == null)
				continue;

			fields.add(field.getName());
		}

		if (clazz.getSuperclass() == null)
			return fields.toArray(new String[0]);

		return findFieldNameForAnnotation(fields, clazz.getSuperclass(), annotationType);
	}
	
	public static Object invoke(Object object, String methodName, Object[] parameters) {
		Class<?> clazz = object.getClass();
		try {
			Method method = clazz.getMethod(methodName, getParameterTypes(parameters));
			return method.invoke(object, parameters);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		
		return null;
	}
	
	private static Class<?>[] getParameterTypes(Object[] parameters) {
		
		List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		
		for(Object parameter : parameters) {
			parameterTypes.add(parameter.getClass());
		}
		return parameterTypes.toArray(new Class<?>[0]);
	}
	
	public static Object get(Object object, String key) {
		
		return get(object, object.getClass(), key);
	}
	
	private static Object get(Object object, Class<?> clazz, String key) {

		try {
			Field field = clazz.getDeclaredField(key);
			field.setAccessible(true);
			return field.get(object);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null)
				get(object, clazz.getSuperclass(), key);
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

		return null;
	}
	
	public static void set(Object object, String key, Object value) {
		
		set(object, object.getClass(), key, value);
	}
	
	public static void set(Object object, Class<?> clazz, String key, Object value) {
		try {
			Field field = clazz.getDeclaredField(key);
			field.setAccessible(true);
			field.set(object, value);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null)
				set(object, clazz.getSuperclass(), key, value);
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}
}
