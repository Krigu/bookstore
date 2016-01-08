package org.books.data.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationInjector {


    public static void injectPersistenceContext(Object bean, EntityManager em) throws IllegalAccessException {
        List<Field> annotatedFields = getAnnotatedFields(bean, PersistenceContext.class);
        for (Field annotatedField : annotatedFields) {
            injectField(annotatedField, bean, em);
        }
    }

    private static void injectField(final Field field, final Object bean, Object instance) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(bean, instance);
    }

    private static List<Field> getAnnotatedFields(final Object bean, Class<? extends Annotation> annotation) {
        final Class<? extends Object> beanClass = bean.getClass();
        final List<Field> annotatedFields = new ArrayList<Field>();
        for (final Field field : beanClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }


}
