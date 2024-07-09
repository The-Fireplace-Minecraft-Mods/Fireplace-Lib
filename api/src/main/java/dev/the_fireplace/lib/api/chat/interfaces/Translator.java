package dev.the_fireplace.lib.api.chat.interfaces;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.UUID;

public interface Translator
{
    Component getTextForTarget(CommandSourceStack target, String translationKey, Object... arguments);

    Component getTextForTarget(CommandSource target, String translationKey, Object... arguments);

    Component getTextForTarget(UUID targetPlayerId, String translationKey, Object... arguments);

    TextComponent getTranslatedText(String translationKey, Object... arguments);

    String getTranslatedString(String translationKey, Object... arguments);

    String getTranslationKeyForTarget(CommandSource target, String translationKey);

    String getTranslationKeyForTarget(UUID targetPlayerId, String translationKey);
}
