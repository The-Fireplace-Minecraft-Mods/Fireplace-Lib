package dev.the_fireplace.lib.chat;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import javax.inject.Singleton;

@Singleton
@Implementation
public final class TextStylesImpl implements TextStyles
{
    private final Style RED = new Style().setColor(Formatting.RED);
    private final Style BLUE = new Style().setColor(Formatting.BLUE);
    private final Style YELLOW = new Style().setColor(Formatting.YELLOW);
    private final Style LIGHT_PURPLE = new Style().setColor(Formatting.LIGHT_PURPLE);
    private final Style GREEN = new Style().setColor(Formatting.GREEN);
    private final Style BLACK = new Style().setColor(Formatting.BLACK);
    private final Style AQUA = new Style().setColor(Formatting.AQUA);
    private final Style DARK_AQUA = new Style().setColor(Formatting.DARK_AQUA);
    private final Style DARK_BLUE = new Style().setColor(Formatting.DARK_BLUE);
    private final Style DARK_GRAY = new Style().setColor(Formatting.DARK_GRAY);
    private final Style DARK_GREEN = new Style().setColor(Formatting.DARK_GREEN);
    private final Style DARK_PURPLE = new Style().setColor(Formatting.DARK_PURPLE);
    private final Style DARK_RED = new Style().setColor(Formatting.DARK_RED);
    private final Style GOLD = new Style().setColor(Formatting.GOLD);
    private final Style GRAY = new Style().setColor(Formatting.GRAY);
    private final Style WHITE = new Style().setColor(Formatting.WHITE);
    private final Style RESET = new Style().setColor(Formatting.RESET);

    @Override
    public Style red() {
        return RED;
    }

    @Override
    public Style blue() {
        return BLUE;
    }

    @Override
    public Style yellow() {
        return YELLOW;
    }

    @Override
    public Style purpleLight() {
        return LIGHT_PURPLE;
    }

    @Override
    public Style green() {
        return GREEN;
    }

    @Override
    public Style black() {
        return BLACK;
    }

    @Override
    public Style aqua() {
        return AQUA;
    }

    @Override
    public Style aquaDark() {
        return DARK_AQUA;
    }

    @Override
    public Style blueDark() {
        return DARK_BLUE;
    }

    @Override
    public Style greyDark() {
        return DARK_GRAY;
    }

    @Override
    public Style greenDark() {
        return DARK_GREEN;
    }

    @Override
    public Style purpleDark() {
        return DARK_PURPLE;
    }

    @Override
    public Style redDark() {
        return DARK_RED;
    }

    @Override
    public Style gold() {
        return GOLD;
    }

    @Override
    public Style grey() {
        return GRAY;
    }

    @Override
    public Style white() {
        return WHITE;
    }

    @Override
    public Style reset() {
        return RESET;
    }
}
