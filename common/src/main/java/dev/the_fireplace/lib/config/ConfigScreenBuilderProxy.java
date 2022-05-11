package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.domain.config.ConfigScreenBuilderFactoryProxy;
import net.minecraft.client.gui.screens.Screen;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Implementation(environment = "CLIENT", allInterfaces = true)
@Singleton
public final class ConfigScreenBuilderProxy implements ConfigScreenBuilderFactory, ConfigScreenBuilderFactoryProxy
{
    private ConfigScreenBuilderFactory activeConfigScreenBuilderFactory;

    @Inject
    public ConfigScreenBuilderProxy(DummyConfigScreenBuilderFactory dummyFactory) {
        this.activeConfigScreenBuilderFactory = dummyFactory;
    }

    @Override
    public Optional<ConfigScreenBuilder> create(Translator translator, String titleTranslationKey, String initialCategoryTranslationKey, Screen parent, Runnable save) {
        return this.activeConfigScreenBuilderFactory.create(translator, titleTranslationKey, initialCategoryTranslationKey, parent, save);
    }

    @Override
    public void setActiveConfigScreenBuilderFactory(ConfigScreenBuilderFactory activeConfigScreenBuilderFactory) {
        this.activeConfigScreenBuilderFactory = activeConfigScreenBuilderFactory;
    }

    @Override
    public boolean hasActiveFactory() {
        return !(this.activeConfigScreenBuilderFactory instanceof DummyConfigScreenBuilderFactory);
    }
}
