package dev.the_fireplace.lib.api.client.injectables;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigScreenBuilderFactory {
    ConfigScreenBuilder create(
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    );
}
