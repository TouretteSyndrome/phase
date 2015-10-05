package org.beatific.flow.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RuntimeAnnotations {

    private static final Constructor<?> annotationInvocationHandlerConstructor;
    private static final Constructor<?> annotationDataConstructor;
    private static final Method annotationDataMethod;
    private static final Field classClassRedefinedCount;
    private static final Field annotationDataAnnotations;
    private static final Field annotationDataDeclaredAnotations;
    private static final Method atomicCasAnnotationData;
    private static final Class<?> atomicClass;

    static{
        // static initialization of necessary reflection Objects
        try {
            Class<?> annotationInvocationHandlerClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
            annotationInvocationHandlerConstructor = annotationInvocationHandlerClass.getDeclaredConstructor(new Class[]{Class.class, Map.class});
            annotationInvocationHandlerConstructor.setAccessible(true);

            atomicClass = Class.forName("java.lang.Class$Atomic");
            Class<?> annotationDataClass = Class.forName("java.lang.Class$AnnotationData");

            annotationDataConstructor = annotationDataClass.getDeclaredConstructor(new Class[]{Map.class, Map.class, int.class});
            annotationDataConstructor.setAccessible(true);
            annotationDataMethod = Class.class.getDeclaredMethod("annotationData");
            annotationDataMethod.setAccessible(true);

            classClassRedefinedCount= Class.class.getDeclaredField("classRedefinedCount");
            classClassRedefinedCount.setAccessible(true);

            annotationDataAnnotations = annotationDataClass.getDeclaredField("annotations");
            annotationDataAnnotations.setAccessible(true);
            annotationDataDeclaredAnotations = annotationDataClass.getDeclaredField("declaredAnnotations");
            annotationDataDeclaredAnotations.setAccessible(true);

            atomicCasAnnotationData = atomicClass.getDeclaredMethod("casAnnotationData", Class.class, annotationDataClass, annotationDataClass);
            atomicCasAnnotationData.setAccessible(true);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
        	e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass, Map<String, Object> valuesMap){
        putAnnotation(c, annotationClass, annotationForMap(annotationClass, valuesMap));
    }

    public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass, T annotation){
        try {
            while (true) { 
                int classRedefinedCount = classClassRedefinedCount.getInt(c);
                Object annotationData = annotationDataMethod.invoke(c);
                Object newAnnotationData = createAnnotationData(c, annotationData, annotationClass, annotation, classRedefinedCount);
                if ((boolean) atomicCasAnnotationData.invoke(atomicClass, c, annotationData, newAnnotationData)) {
                    break;
                }
            }
        } catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException | InstantiationException e){
            throw new IllegalStateException(e);
        }

    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> Object /*AnnotationData*/ createAnnotationData(Class<?> c, Object /*AnnotationData*/ annotationData, Class<T> annotationClass, T annotation, int classRedefinedCount) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) annotationDataAnnotations.get(annotationData);
        Map<Class<? extends Annotation>, Annotation> declaredAnnotations= (Map<Class<? extends Annotation>, Annotation>) annotationDataDeclaredAnotations.get(annotationData);

        Map<Class<? extends Annotation>, Annotation> newDeclaredAnnotations = new LinkedHashMap<>(annotations);
        newDeclaredAnnotations.put(annotationClass, annotation);
        Map<Class<? extends Annotation>, Annotation> newAnnotations ;
        if (declaredAnnotations == annotations) {
            newAnnotations = newDeclaredAnnotations;
        } else{
            newAnnotations = new LinkedHashMap<>(annotations);
            newAnnotations.put(annotationClass, annotation);
        }
        return annotationDataConstructor.newInstance(newAnnotations, newDeclaredAnnotations, classRedefinedCount);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T annotationForMap(final Class<T> annotationClass, final Map<String, Object> valuesMap){
        return (T)AccessController.doPrivileged(new PrivilegedAction<Annotation>(){
            public Annotation run(){
                InvocationHandler handler;
                try {
                    handler = (InvocationHandler) annotationInvocationHandlerConstructor.newInstance(annotationClass,new HashMap<>(valuesMap));
                } catch (InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
                return (Annotation)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass }, handler);
            }
        });
    }
}