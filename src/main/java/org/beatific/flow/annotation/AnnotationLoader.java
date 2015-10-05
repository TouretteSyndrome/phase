package org.beatific.flow.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.beatific.flow.properties.PropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnotationLoader {
	
	private final Map<String, Object> members = new HashMap<String, Object>();
	
	@Autowired
	PropertiesConverter converter = new PropertiesConverter();

	public synchronized void load(Class<?> clazz, Class<? extends Annotation> annotationClass) 
    {
		loadAnnoation(clazz, (Class<? extends Annotation>) annotationClass);
		RuntimeAnnotations.putAnnotation(clazz, annotationClass, members);
    }
	
	public void loadAnnoation (Class<?> clazz, final Class<? extends Annotation> annotationClass) {

		members.clear();
		
		Annotation annotaion = clazz.getAnnotation(annotationClass);
		
		if (!annotationClass.isAnnotation())
			throw new IllegalArgumentException("Not an annotation type");

		Method[] methods = AccessController
				.doPrivileged(new PrivilegedAction<Method[]>() {
					public Method[] run() {
						// Initialize memberTypes and defaultValues
						return annotationClass.getDeclaredMethods();
					}
				});

		for (Method method : methods) {
			if (method.getParameterTypes().length != 0)
				throw new IllegalArgumentException(method + " has params");
			String name = method.getName();
			
			Object result = null;
			try {
				result = method.invoke(annotaion, new Object[0]);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			if(result == null)result = method.getDefaultValue();
			
			if(result instanceof String)result = converter.convert((String)result);
			
			members.put(name, result);
		}
	}

	public Map<String, Object> members() {
		return members;
	}


	@Override
	public String toString() {
		return "AnnotationClass [members=" + members + "]";
	}

	
}
