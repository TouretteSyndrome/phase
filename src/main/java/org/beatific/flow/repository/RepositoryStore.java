package org.beatific.flow.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beatific.flow.annotation.AnnotationMap;
import org.beatific.flow.common.AutoDataResolver;
import org.beatific.flow.repository.support.CopyFrom;
import org.beatific.flow.repository.support.CopyTo;
import org.beatific.flow.repository.support.IdLocal;
import org.beatific.flow.repository.support.OneState;
import org.beatific.flow.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryStore {

	@Autowired
	private AnnotationMap aMap;
	private Map<String, Repository<?>> store = new HashMap<String, Repository<?>>();
	private List<Object> objects;;

	private synchronized Repository<?> getRepository(Object object) {
		Repository<?> repository = store.get(id(object));
		if(repository == null) {
			repository = new DefaultRepository();
			store.put(id(object), repository);
		}
		
		return repository;
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
			if(objects == null) objects = new ArrayList<Object>();
			for(Object object : objects) {
				if(object instanceof Repository) {
					addRepository((Repository<?>)object);
				}
			}
		}
		
	}
	
	private class DefaultRepository implements Repository<OneState> {

		protected IdLocal<Map<String, Object>> idStore = new IdLocal<Map<String, Object>>() {
			
			protected Map<String, Object> initialValue() {
				return new HashMap<String, Object>();
			}
		};
		
		protected Map<String, Object> dataMap(Object object) {
			return idStore.get(object);
		}
		
		public OneState getState() {
			return OneState.ONE;
		}
		
		public void save(Object state, Object object) {
			
			if(object instanceof AutoDataResolver) {
				AutoDataResolver resolver = (AutoDataResolver)object;
				
				for(String fieldName : ReflectionUtils.findFieldNameForAnnotation(resolver.getClass(), CopyFrom.class)) {
					dataMap(object).put(fieldName, resolver.get(fieldName));
					resolver.put(fieldName, null); /* CopyFrom을 사용하는 변수의 초기값은 null이어야 한다. */
				}
			}
		}
		
		public Object load(Object state, Object object) {
			
			if(object instanceof AutoDataResolver) {
				AutoDataResolver resolver = (AutoDataResolver)object;
				
				for(String fieldName : ReflectionUtils.findFieldNameForAnnotation(resolver.getClass(), CopyTo.class)) {
					Object fieldValue = dataMap(object).get(fieldName);
					
					if(fieldValue == null) continue;
					resolver.put(fieldName, fieldValue);
				}
			}
			
			return dataMap(object);
		}

		
		public void change(Object state, Object object) {
			
		}
		
		public void remove(Object state, Object object) {
			dataMap(object).clear();
		}
		
	}

}
