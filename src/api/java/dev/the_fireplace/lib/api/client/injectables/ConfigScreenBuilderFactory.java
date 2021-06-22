package dev.the_fireplace.lib.api.client.injectables;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public interface ConfigScreenBuilderFactory {
    ConfigScreenBuilder create(Translator translator, ConfigEntryBuilder entryBuilder, ConfigCategory initialCategory);
}
