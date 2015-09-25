package org.beatific.flow.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Invoker {

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
}
