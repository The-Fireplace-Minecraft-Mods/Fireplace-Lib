package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.config.dummy.DummyConfigScreenBuilderFactory;
import net.minecraft.client.gui.screens.Screen;

import javax.inject.Inject;
import javax.inject.Singleton;

@Implementation(environment = "CLIENT")
@Singleton
public final class ConfigScreenBuilderProxy implements ConfigScreenBuilderFactory
{
    private ConfigScreenBuilderFactory activeConfigScreenBuilderFactory;

    @Inject
    public ConfigScreenBuilderProxy(DummyConfigScreenBuilderFactory dummyFactory) {
        this.activeConfigScreenBuilderFactory = dummyFactory;
    }

    @Override
    public ConfigScreenBuilder create(Translator translator, String titleTranslationKey, String initialCategoryTranslationKey, Screen parent, Runnable save) {
        return this.activeConfigScreenBuilderFactory.create(translator, titleTranslationKey, initialCategoryTranslationKey, parent, save);
    }

    public void setActiveConfigScreenBuilderFactory(ConfigScreenBuilderFactory activeConfigScreenBuilderFactory) {
        this.activeConfigScreenBuilderFactory = activeConfigScreenBuilderFactory;
    }
}
