package dev.the_fireplace.lib.config.cloth.optionbuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import dev.the_fireplace.lib.config.cloth.ClothParameterTypeConverter;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;

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
        this.typeConverter = new OptionTypeConverter<S, T>() {
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
        setDescriptionRowCount((byte) 1);
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
        setDescriptionRowCount((byte) 1);
    }

    protected void setDefaultValue(S defaultValue) {
        T clothDefaultValue = typeConverter.convertToClothType(defaultValue);
        try {
            Method setDefaultValue = findSingleParameterMethod("setDefaultValue", clothDefaultValue.getClass());
            setDefaultValue.invoke(fieldBuilder, clothDefaultValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set default value for field builder of type " + fieldBuilder.getClass() + " with default value type " + clothDefaultValue.getClass(), e);
            FireplaceLib.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
    }

    private void setSaveConsumer(Consumer<S> saveConsumer) {
        Consumer<T> clothSaveConsumer = value -> saveConsumer.accept(typeConverter.convertFromClothType(value));
        try {
            Method setSaveConsumer = findSingleParameterMethod("setSaveConsumer", clothSaveConsumer.getClass());
            setSaveConsumer.invoke(fieldBuilder, clothSaveConsumer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set save consumer for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
    }

    @Override
    public OptionBuilder<S> setDescriptionRowCount(byte descriptionRowCount) {
        try {
            Method setTooltip = fieldBuilder.getClass().getMethod("setTooltip", String[].class);
            String descriptionTranslationKey = optionTranslationBase + ".desc";
            if (descriptionRowCount == 1) {
                setTooltip.invoke(fieldBuilder, (Object) new String[]{translator.getTranslatedString(descriptionTranslationKey)});
            } else {
                setTooltip.invoke(fieldBuilder, (Object) generateMultilineDescription(descriptionTranslationKey, descriptionRowCount));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set tooltip for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }

    private String[] generateMultilineDescription(String baseKey, int count) {
        List<String> texts = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            texts.add(translator.getTranslatedString(baseKey + "[" + i + "]"));
        }
        return texts.toArray(new String[0]);
    }

    @Override
    public OptionBuilder<S> setErrorSupplier(Function<S, Optional<Text>> errorSupplier) {
        Function<T, Optional<Text>> clothErrorSupplier = value -> errorSupplier.apply(typeConverter.convertFromClothType(value));
        try {
            Method setErrorSupplier = findSingleParameterMethod("setErrorSupplier", clothErrorSupplier.getClass());
            setErrorSupplier.invoke(fieldBuilder, clothErrorSupplier);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set error supplier for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().error(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }

    protected Method findSingleParameterMethod(String methodName, Class<?> parameterClass) throws NoSuchMethodException {
        Iterator<Class<?>> checkClasses = ClothParameterTypeConverter.getPotentialClasses(parameterClass);
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
