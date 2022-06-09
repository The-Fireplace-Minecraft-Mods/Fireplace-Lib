package dev.the_fireplace.lib.api.chat.interfaces;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

import java.util.UUID;

public interface Translator
{
    MutableComponent getTextForTarget(CommandSourceStack target, String translationKey, Object... args);

    MutableComponent getTextForTarget(CommandSource target, String translationKey, Object... args);

    MutableComponent getTextForTarget(UUID target, String translationKey, Object... args);

    MutableComponent getTranslatedText(String translationKey, Object... args);

    String getTranslatedString(String translationKey, Object... args);

    String getTranslationKeyForTarget(CommandSource target, String translationKey);

    String getTranslationKeyForTarget(UUID target, String translationKey);
}
