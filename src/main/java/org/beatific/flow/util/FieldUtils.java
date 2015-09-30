package org.beatific.flow.util;

import java.io.IOException;
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

public class FieldUtils {

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
	
	public static List<Class<?>> findClassByMemberValue(String[] basePackages, Class<?> fieldType) {
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
							
							for(Field field : clazz.getFields()) {
								if(field.getName() == fieldType.getName()) {
									classes.add(clazz);
									break;
								}
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
