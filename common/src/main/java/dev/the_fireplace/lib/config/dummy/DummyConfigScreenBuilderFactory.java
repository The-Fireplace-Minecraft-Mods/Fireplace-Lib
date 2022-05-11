package dev.the_fireplace.lib.config.dummy;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import net.minecraft.client.gui.screens.Screen;

public final class DummyConfigScreenBuilderFactory implements ConfigScreenBuilderFactory
{
    @Override
    public ConfigScreenBuilder create(Translator translator, String titleTranslationKey, String initialCategoryTranslationKey, Screen parent, Runnable save) {
        return new DummyConfigScreenBuilder();
    }
}
