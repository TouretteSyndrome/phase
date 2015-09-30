package org.beatific.flow.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class AnnotationUtils {

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
	
	private static String resolveBasePackage(String basePackage) {
		if(basePackage == null)return "";
		return ClassUtils.convertClassNameToResourcePath(basePackage);
	}
	
	private static Class<?> getClass(MetadataReader metadataReader) throws ClassNotFoundException, LinkageError {
		return ClassUtils.forName(metadataReader.getClassMetadata().getClassName(), ClassUtils.getDefaultClassLoader());
	}
	
	private static boolean isClassWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		return clazz.getAnnotation(annotation) != null;
	}
	
	private static boolean isClassWithAnnotationField(Class<?> clazz, Class<? extends Annotation> annotation) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) 
			if(field.getAnnotation(annotation) != null) return true;
		return false;
	}
	
	public static List<Class<?>> findClassByAnnotation(Class<? extends Annotation> annotationType) {
		return findClassByAnnotation(new String(), annotationType);
	}
	
	public static List<Class<?>> findClassByAnnotation(String basePackage, Class<? extends Annotation> annotationType) {
		String[] basePackages;
		
		if(basePackage.contains(",")) {
			basePackages = basePackage.split(",");
		} else {
			basePackages = new String[]{basePackage};
		}
		
		return findClassByAnnotation(basePackages, annotationType);
	}
	
	public static List<Class<?>> findClassByAnnotation(String[] basePackages, Class<? extends Annotation> annotationType) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for(String basePackage : basePackages)
			try {
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
				Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						try {
							MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
							Class<?> clazz = getClass(metadataReader);
							if (isClassWithAnnotation(clazz, annotationType)) {
								classes.add(clazz);
							}
						}catch (Throwable ex) {
							throw new BeanDefinitionStoreException(
									"Failed to read class: " + resource, ex);
						}
					}
				}
			}
			catch (IOException ex) {
				throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
			}
		return classes;
	}
	
	public static List<Class<?>> findClassByFieldAnnotation(String[] basePackages, Class<? extends Annotation> annotationType) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for(String basePackage : basePackages)
			try {
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
				Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						try {
							MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
							Class<?> clazz = getClass(metadataReader);
							if (isClassWithAnnotationField(clazz, annotationType)) {
								classes.add(clazz);
							}
						}catch (Throwable ex) {
							throw new BeanDefinitionStoreException(
									"Failed to read class: " + resource, ex);
						}
					}
				}
			}
			catch (IOException ex) {
				throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
			}
		return classes;
	}
	
}
