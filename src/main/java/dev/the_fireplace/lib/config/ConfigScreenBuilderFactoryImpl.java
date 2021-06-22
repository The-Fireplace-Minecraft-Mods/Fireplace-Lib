package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

@Implementation
public final class ConfigScreenBuilderFactoryImpl implements ConfigScreenBuilderFactory {
    @Override
    public ConfigScreenBuilder create(Translator translator, ConfigEntryBuilder entryBuilder, ConfigCategory initialCategory) {
        return new ConfigScreenBuilderImpl(translator, entryBuilder, initialCategory);
    }
}
