package org.beatific.flow.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

import org.beatific.flow.map.MultiMap;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AnnotationMap collect all spring beans that write a special annotation. 
 * and the beans is registered to Map.
 * 
 * @author beatific J
 *
 */
@Component
public class AnnotationMap {

	@Autowired
	private BeanFactory beanFactory;

	private MultiMap<Class<? extends Annotation>, Object> map = null;

	@Value("${basePackage}")
	private String basePackage;

	private List<Class<?>> classes(Class<? extends Annotation> annotation) {
		
		StringBuffer sb = new StringBuffer("org.beatific.flow");
		if(basePackage != null) sb.append(",").append(basePackage);
		
		return AnnotationUtils.findClassByAnnotation(sb.toString(), annotation);
	}

	@SuppressWarnings("unchecked")
	private void initialize() {

		map = new MultiMap<Class<? extends Annotation>, Object>();
		List<Class<?>> classes = (List<Class<?>>) classes(AnnotationCollect.class);

		for (Class<?> clazz : classes) {

			if (clazz.isAnnotation()) {
				Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) clazz;
				
				for (Class<?> klass : classes(annotationClass)) {
					map.put(annotationClass,  beanFactory.getBean(klass));
				}
			}
		}
	}
	
	public List<Object> get(Class<? extends Annotation> annotation) {
		if(map == null)initialize();
		return map.get(annotation);
	}
}
