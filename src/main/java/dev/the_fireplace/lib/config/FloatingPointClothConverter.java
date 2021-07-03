package dev.the_fireplace.lib.config;

public final class FloatingPointClothConverter {
    static float guiValueToFloat(Long guiValue) {
        return guiValue / 1000f;
    }

    static long floatToGuiValue(float value) {
        return (long) (value * 1000);
    }

    static double guiValueToDouble(Long guiValue, byte precision) {
        double factor = Math.pow(10, precision);
        return guiValue / factor;
    }

    static long doubleToGuiValue(double value, byte precision) {
        double factor = Math.pow(10, precision);
        return (long) (value * factor);
    }
}
