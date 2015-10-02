package org.beatific.flow.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.beatific.flow.annotation.AnnotationUtils;
import org.beatific.flow.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConverter {

	@Autowired
	private Environment props;
	
	@Value("${basePackage}")
	private String basePackage;
	
	public String convert(String str) {
		
		String key = key(str);
		if(key == null) return str;
		return (String)props.getProperty(key);
	}
	
	private String key(String str) {
		Parser parser = new Parser("\\$\\{(\\w*)\\}");
		return parser.parseFirstOne(str);
	}
	
	private List<Class<?>> classes(Class<? extends Annotation> annotation) {
		
		StringBuffer sb = new StringBuffer("org.beatific.flow");
		if(basePackage != null) sb.append(",").append(basePackage);
		
		return AnnotationUtils.findClassByAnnotation(sb.toString(), annotation);
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		
		List<Class<?>> classes = (List<Class<?>>) classes(PropertiesConvert.class);
		
		for (Class<?> clazz : classes) {

			if (clazz.isAnnotation()) {
				Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) clazz;
				Method[] methods = annotationClass.getDeclaredMethods();
				for(Method method : methods) {
					if(method.getGenericReturnType() == String.class) {
						
						String value = null;
						try {
							value = (String)method.invoke(annotationClass, new Object[0]);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}

			}
		}
	}
}
