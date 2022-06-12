package dev.the_fireplace.lib.api.chat.interfaces;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.UUID;

public interface Translator
{
    Component getTextForTarget(CommandSourceStack target, String translationKey, Object... args);

    Component getTextForTarget(CommandSource target, String translationKey, Object... args);

    Component getTextForTarget(UUID target, String translationKey, Object... args);

    TextComponent getTranslatedText(String translationKey, Object... args);

    String getTranslatedString(String translationKey, Object... args);

    String getTranslationKeyForTarget(CommandSource target, String translationKey);

    String getTranslationKeyForTarget(UUID target, String translationKey);
}
