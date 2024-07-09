package dev.the_fireplace.lib.api.chat.interfaces;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.util.UUID;

public interface Translator
{
    MutableComponent getTextForTarget(CommandSourceStack target, String translationKey, Object... arguments);

    MutableComponent getTextForTarget(CommandSource target, String translationKey, Object... arguments);

    MutableComponent getTextForTarget(UUID targetPlayerId, String translationKey, Object... arguments);

    TextComponent getTranslatedText(String translationKey, Object... arguments);

    String getTranslatedString(String translationKey, Object... arguments);

    String getTranslationKeyForTarget(CommandSource target, String translationKey);

    String getTranslationKeyForTarget(UUID targetPlayerId, String translationKey);
}
