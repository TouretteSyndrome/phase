package org.beatific.flow.phase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.beatific.flow.annotation.AnnotationCollect;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationCollect
public @interface Phase {

	String id();
	int order();
}
