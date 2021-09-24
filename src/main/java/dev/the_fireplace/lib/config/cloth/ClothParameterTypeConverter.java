package dev.the_fireplace.lib.config.cloth;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ClassUtils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ClothParameterTypeConverter {
    public static Iterator<Class<?>> getPotentialClasses(Class<?> parameterClass) {
        List<Class<?>> potentialClasses = Lists.newArrayList(parameterClass);
        Class<?> primitiveClass = ClassUtils.wrapperToPrimitive(parameterClass);
        if (primitiveClass != null) {
            potentialClasses.add(primitiveClass);
        }
        if (parameterClass.getName().contains("$$Lambda$")) {
            potentialClasses.add(Consumer.class);
            potentialClasses.add(Function.class);
        }
        potentialClasses.add(Object.class);

        return potentialClasses.iterator();
    }
}
