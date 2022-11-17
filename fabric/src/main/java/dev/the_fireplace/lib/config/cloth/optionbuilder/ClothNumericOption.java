package dev.the_fireplace.lib.config.cloth.optionbuilder;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.NumericOptionBuilder;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ClothNumericOption<S, T> extends ClothGenericOption<S, T> implements NumericOptionBuilder<S>
{
    public ClothNumericOption(Translator translator, FieldBuilder<S, ?, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction) {
        super(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction);
    }

    public ClothNumericOption(Translator translator, FieldBuilder<T, ?, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction, OptionTypeConverter<S, T> typeConverter) {
        super(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction, typeConverter);
    }

    @Override
    public NumericOptionBuilder<S> setMinimum(S minimum) {
        T clothMinimum = typeConverter.convertToClothType(minimum);
        String[] minimumFunctionNames = {"setMin", "setMinimum"};
        boolean foundFunction = false;
        for (String functionName : minimumFunctionNames) {
            try {
                Method setMinimum = findSingleParameterMethod(functionName, clothMinimum.getClass());
                setMinimum.invoke(fieldBuilder, clothMinimum);
                foundFunction = true;
                break;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

            }
        }
        if (!foundFunction) {
            FireplaceLibConstants.getLogger().error("Unable to set minimum for field builder of type " + fieldBuilder.getClass() + " with target type " + clothMinimum.getClass());
            FireplaceLibConstants.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }

    @Override
    public NumericOptionBuilder<S> setMaximum(S maximum) {
        T clothMaximum = typeConverter.convertToClothType(maximum);
        String[] maximumFunctionNames = {"setMax", "setMaximum"};
        boolean foundFunction = false;
        for (String functionName : maximumFunctionNames) {
            try {
                Method setMaximum = findSingleParameterMethod(functionName, clothMaximum.getClass());
                setMaximum.invoke(fieldBuilder, clothMaximum);
                foundFunction = true;
                break;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

            }
        }
        if (!foundFunction) {
            FireplaceLibConstants.getLogger().error("Unable to set maximum for field builder of type " + fieldBuilder.getClass() + " with target type " + clothMaximum.getClass());
            FireplaceLibConstants.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }
}
