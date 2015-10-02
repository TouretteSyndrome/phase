package org.beatific.flow.properties;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PropertiesHandler implements InvocationHandler {

	private PropertiesConverter converter;
	private Object object = null;
	
	public PropertiesHandler(Object object) {
		this.object = object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		Object result = method.invoke(object, args);
		
		if(result instanceof String) {
			return converter.convert((String)result);
		} 
		return result;
	}

}
