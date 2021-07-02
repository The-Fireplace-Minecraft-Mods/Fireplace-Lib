package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import net.minecraft.client.gui.screen.Screen;

@Implementation
public final class ClothConfigScreenBuilderFactory implements ConfigScreenBuilderFactory {
    @Override
    public ConfigScreenBuilder create(
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    ) {
        return new ClothConfigScreenBuilder(translator, titleTranslationKey, initialCategoryTranslationKey, parent, save);
    }
}
