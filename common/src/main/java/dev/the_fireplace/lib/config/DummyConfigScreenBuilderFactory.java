package dev.the_fireplace.lib.config;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;

public final class DummyConfigScreenBuilderFactory implements ConfigScreenBuilderFactory
{
    @Override
    public Optional<ConfigScreenBuilder> create(Translator translator, String titleTranslationKey, String initialCategoryTranslationKey, Screen parent, Runnable save) {
        return Optional.empty();
    }
}
