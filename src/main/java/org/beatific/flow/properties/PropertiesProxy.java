package org.beatific.flow.properties;

import java.lang.reflect.Proxy;

import org.springframework.context.annotation.Configuration;


public class PropertiesProxy {
	
	
	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> clazz) {

		T object = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new PropertiesHandler(clazz));

		return object;

	}
}
