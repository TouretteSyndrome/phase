package org.beatific.flow.phase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.beatific.flow.annotation.AnnotationCollect;
import org.beatific.flow.properties.PropertiesConvert;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PropertiesConvert
@AnnotationCollect
public @interface Phase {

	String id();
	int order();
	int fixedDelay() default 0;
	String fixedDelayString() default "";
	String cron() default "";
}
