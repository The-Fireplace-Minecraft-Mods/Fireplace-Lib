package dev.the_fireplace.lib.domain.config;

public interface OptionTypeConverter<S, T> {
    T convertToClothType(S source);

    S convertFromClothType(T clothValue);
}
