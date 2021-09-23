package dev.the_fireplace.lib.config.cloth.optionbuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @param <S> Source type - what's in the config
 * @param <T> Target Cloth type - what type Cloth Config actually accepts
 */
public class ClothGenericOption<S, T> implements OptionBuilder<S> {
    private final Translator translator;
    protected final FieldBuilder<T, ?> fieldBuilder;
    private final String optionTranslationBase;
    private final Map<OptionBuilder<?>, Predicate<?>> dependencies = Maps.newConcurrentMap();
    protected final OptionTypeConverter<S, T> typeConverter;

    public ClothGenericOption(
        Translator translator,
        FieldBuilder<S, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction
    ) {
        this.translator = translator;
        try {
            //noinspection unchecked
            this.fieldBuilder = (FieldBuilder<T, ?>) fieldBuilder;
        } catch (ClassCastException e) {
            throw new IllegalStateException("No type converter provided for Cloth Option!", e);
        }
        this.optionTranslationBase = optionTranslationBase;
        this.typeConverter = new OptionTypeConverter<>() {
            @Override
            public T convertToClothType(S source) {
                //noinspection unchecked
                return (T) source;
            }

            @Override
            public S convertFromClothType(T clothValue) {
                //noinspection unchecked
                return (S) clothValue;
            }
        };
        setDefaultValue(defaultValue);
        setSaveConsumer(saveFunction);
    }

    public ClothGenericOption(
        Translator translator,
        FieldBuilder<T, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction,
        OptionTypeConverter<S, T> typeConverter
    ) {
        this.translator = translator;
        this.fieldBuilder = fieldBuilder;
        this.optionTranslationBase = optionTranslationBase;
        this.typeConverter = typeConverter;
        setDefaultValue(defaultValue);
        setSaveConsumer(saveFunction);
    }

    protected void setDefaultValue(S defaultValue) {
        T clothDefaultValue = typeConverter.convertToClothType(defaultValue);
        try {
            Method setDefaultValue = findMethod("setDefaultValue", clothDefaultValue.getClass());
            setDefaultValue.invoke(fieldBuilder, clothDefaultValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set default value for field builder of type " + fieldBuilder.getClass() + " with default value type " + clothDefaultValue.getClass(), e);
            FireplaceLib.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
    }

    private void setSaveConsumer(Consumer<S> saveConsumer) {
        Consumer<T> clothSaveConsumer = value -> saveConsumer.accept(typeConverter.convertFromClothType(value));
        try {
            Method setSaveConsumer = findMethod("setSaveConsumer", clothSaveConsumer.getClass());
            setSaveConsumer.invoke(fieldBuilder, clothSaveConsumer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set save consumer for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().error(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));//TODO trace
        }
    }

    @Override
    public OptionBuilder<S> setDescriptionRowCount(byte descriptionRowCount) {
        try {
            Method setTooltip = fieldBuilder.getClass().getMethod("setTooltip", Text[].class);
            String descriptionTranslationKey = optionTranslationBase + ".desc";
            if (descriptionRowCount == 1) {
                setTooltip.invoke(fieldBuilder, (Object) new Text[]{translator.getTranslatedText(descriptionTranslationKey)});
            } else {
                setTooltip.invoke(fieldBuilder, (Object) generateMultilineDescription(descriptionTranslationKey, descriptionRowCount));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set tooltip for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }

    private Text[] generateMultilineDescription(String baseKey, int count) {
        List<Text> texts = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            texts.add(translator.getTranslatedText(baseKey + "[" + i + "]"));
        }
        return texts.toArray(new Text[0]);
    }

    @Override
    public OptionBuilder<S> setErrorSupplier(Function<S, Optional<Text>> errorSupplier) {
        Function<T, Optional<Text>> clothErrorSupplier = value -> errorSupplier.apply(typeConverter.convertFromClothType(value));
        try {
            Method setErrorSupplier = findMethod("setErrorSupplier", clothErrorSupplier.getClass());
            setErrorSupplier.invoke(fieldBuilder, clothErrorSupplier);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set error supplier for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().error(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }

    protected Method findMethod(String methodName, Class<?> parameterClass) throws NoSuchMethodException {
        Iterator<Class<?>> checkClasses = getPotentialClasses(parameterClass);
        while (true) {
            try {
                Class<?> testClass = checkClasses.next();
                return fieldBuilder.getClass().getMethod(methodName, testClass);
            } catch (NoSuchMethodException e) {
                if (checkClasses.hasNext()) {
                    continue;
                }
                throw e;
            }
        }
    }

    private Iterator<Class<?>> getPotentialClasses(Class<?> parameterClass) {
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

    @Override
    public OptionBuilder<S> addDependency(OptionBuilder<Boolean> parent) {
        return addDependency(parent, parentValue -> parentValue);
    }

    @Override
    public <U> OptionBuilder<S> addDependency(OptionBuilder<U> parent, Predicate<U> dependencyMet) {
        dependencies.put(parent, dependencyMet);
        return this;
    }

    public final FieldBuilder<T, ?> getFieldBuilder() {
        return fieldBuilder;
    }

    public OptionTypeConverter<S, T> getTypeConverter() {
        return typeConverter;
    }

    public Map<OptionBuilder<?>, Predicate<?>> getDependencies() {
        return dependencies;
    }
}
