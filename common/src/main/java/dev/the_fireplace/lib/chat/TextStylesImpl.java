package dev.the_fireplace.lib.chat;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

import javax.inject.Singleton;

@Singleton
@Implementation
public final class TextStylesImpl implements TextStyles
{
    private final Style RED = Style.EMPTY.withColor(ChatFormatting.RED);
    private final Style BLUE = Style.EMPTY.withColor(ChatFormatting.BLUE);
    private final Style YELLOW = Style.EMPTY.withColor(ChatFormatting.YELLOW);
    private final Style LIGHT_PURPLE = Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);
    private final Style GREEN = Style.EMPTY.withColor(ChatFormatting.GREEN);
    private final Style BLACK = Style.EMPTY.withColor(ChatFormatting.BLACK);
    private final Style AQUA = Style.EMPTY.withColor(ChatFormatting.AQUA);
    private final Style DARK_AQUA = Style.EMPTY.withColor(ChatFormatting.DARK_AQUA);
    private final Style DARK_BLUE = Style.EMPTY.withColor(ChatFormatting.DARK_BLUE);
    private final Style DARK_GRAY = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
    private final Style DARK_GREEN = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
    private final Style DARK_PURPLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE);
    private final Style DARK_RED = Style.EMPTY.withColor(ChatFormatting.DARK_RED);
    private final Style GOLD = Style.EMPTY.withColor(ChatFormatting.GOLD);
    private final Style GRAY = Style.EMPTY.withColor(ChatFormatting.GRAY);
    private final Style WHITE = Style.EMPTY.withColor(ChatFormatting.WHITE);
    private final Style RESET = Style.EMPTY.withColor(ChatFormatting.RESET);

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
