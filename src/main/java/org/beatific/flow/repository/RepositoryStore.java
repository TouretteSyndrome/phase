package org.beatific.flow.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beatific.flow.annotation.AnnotationMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryStore {

	@Autowired
	private AnnotationMap aMap;
	private Map<String, Repository<?>> store = new HashMap<String, Repository<?>>();
	private List<Object> objects;;

	private synchronized Repository<?> getRepository(Object object) {
		return store.get(id(object));
	}

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
	
	private synchronized void addRepository(Repository<?> repository) {
		
		store.put(id(repository), repository);
	}

	public void save(Object object) {

		Repository<?> repository = getRepository(object);
		if(repository == null) throw new NullPointerException("Repository is not Found for Class[" + object.getClass() + "]");
		repository.save(repository.getState(), object);
	}

	public Object load(Object object) {

		Repository<?> repository = getRepository(object);
		return repository.load(repository.getState(), object);
	}

	public void change(Object object) {

		Repository<?> repository = getRepository(object);
		repository.change(repository.getState(), object);
	}

	public void remove(Object object) {

		Repository<?> repository = getRepository(object);
		repository.remove(repository.getState(), object);
	}

	public void loadStore() {
		
		if(objects == null) {
			objects = aMap.get(Store.class);
			if(objects == null) throw new RepositoryLoadException("Repository that 'Store' annotation tagged is not existed");
		}
		
		for(Object object : objects) {
			if(object instanceof Repository) {
				addRepository((Repository<?>)object);
			}
		}
	}

}
