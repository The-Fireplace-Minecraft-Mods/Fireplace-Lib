package dev.the_fireplace.lib.api.chat.injectables;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;

import java.util.Collection;

public interface TranslatorFactory {
    /**
     * Properly initializes your translation service.
     * This should happen on both client (If the mod is present there) and server to ensure proper synchronization.
     * {@link net.fabricmc.api.ModInitializer#onInitialize}/{@link dev.the_fireplace.annotateddi.api.entrypoints.DIModInitializer#onInitialize} is a good place to do so.
     */
    void addTranslator(String modid);

    Translator getTranslator(String modid);

    Collection<String> availableTranslators();
}
