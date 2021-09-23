package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.lib.domain.config.OptionTypeConverter;

public final class FloatingPointClothConverter<T extends Number> implements OptionTypeConverter<T, Long> {
    public static final byte INITIAL_PRECISION = 1;

    private byte precision = INITIAL_PRECISION;

    @Override
    public Long convertToClothType(T source) {
        double factor = Math.pow(10, precision);
        return (long) (source.doubleValue() * factor);
    }

    @Override
    public T convertFromClothType(Long clothValue) {
        double factor = Math.pow(10, precision);
        //noinspection unchecked
        return (T) (Double) (clothValue / factor);
    }

    public void setPrecision(byte precision) {
        this.precision = precision;
    }
}
