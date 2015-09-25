package org.beatific.flow.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.beatific.flow.annotation.AnnotationCollect;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationCollect
public @interface Store {

	String id();
}
