package org.beatific.flow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RuntimeAnnotationsTests {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface TestAnnotation {
		String value();
	}

	public static class TestClass {
	}

	@Test
	public void testPutAnnotation() {
		TestAnnotation annotation = TestClass.class.getAnnotation(TestAnnotation.class);
		assertThat(annotation, nullValue());
		
		Map<String, Object> valuesMap = new HashMap<>();
		valuesMap.put("value", "some String");
		
		RuntimeAnnotations.putAnnotation(TestClass.class, TestAnnotation.class, valuesMap);
		
		annotation = TestClass.class.getAnnotation(TestAnnotation.class);
		
		assertThat(annotation.value(), is("some String"));
	}
}