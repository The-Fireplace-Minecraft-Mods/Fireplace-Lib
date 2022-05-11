package dev.the_fireplace.lib.config.cloth.optionbuilder;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.DecimalSliderOptionBuilder;
import dev.the_fireplace.lib.config.cloth.FloatingPointClothConverter;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClothDecimalSliderOption<S, T> extends ClothNumericOption<S, T> implements DecimalSliderOptionBuilder<S>
{
    private final S originalMinimum;
    private final S originalMaximum;
    private final S originalCurrent;
    private final S originalDefault;
    private byte precision = FloatingPointClothConverter.INITIAL_PRECISION;
    private boolean isPercent = false;

    public ClothDecimalSliderOption(
        Translator translator,
        FieldBuilder<T, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction,
        S currentValue,
        S minimum,
        S maximum,
        OptionTypeConverter<S, T> typeConverter
    ) {
        super(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction, typeConverter);
        this.originalMinimum = minimum;
        this.originalMaximum = maximum;
        this.originalCurrent = currentValue;
        this.originalDefault = defaultValue;
        resetDisplayPrecision();
    }

    @Override
    public DecimalSliderOptionBuilder<S> setPrecision(byte precision) {
        this.precision = precision;
        resetSliderToPrecision(precision);
        resetDisplayPrecision();
        return this;
    }

    @Override
    public void enablePercentMode() {
        isPercent = true;
    }

    private void resetSliderToPrecision(byte precision) {
        if (this.typeConverter instanceof FloatingPointClothConverter) {
            ((FloatingPointClothConverter<?>) this.typeConverter).setPrecision(precision);
            setMinimum(originalMinimum);
            setMaximum(originalMaximum);
            setDefaultValue(originalDefault);
            setCurrentValue(originalCurrent);
        }
    }

    protected void setCurrentValue(S currentValue) {
        T clothCurrentValue = typeConverter.convertToClothType(currentValue);
        try {
            Field currentValueField = fieldBuilder.getClass().getField("value");
            currentValueField.set(fieldBuilder, clothCurrentValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            FireplaceLibConstants.getLogger().error("Unable to set current value for field builder of type " + fieldBuilder.getClass() + " with current value type " + clothCurrentValue.getClass(), e);
            FireplaceLibConstants.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getFields()));
        }
    }

    protected void resetDisplayPrecision() {
        Function<T, Component> textGetter = value -> Component.nullToEmpty(String.format("%." + precision + "f", typeConverter.convertFromClothType(value)) + (isPercent ? "%" : ""));
        try {
            Method setDefaultValue = findSingleParameterMethod("setTextGetter", textGetter.getClass());
            setDefaultValue.invoke(fieldBuilder, textGetter);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLibConstants.getLogger().error("Unable to set display precision for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLibConstants.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
    }
}
