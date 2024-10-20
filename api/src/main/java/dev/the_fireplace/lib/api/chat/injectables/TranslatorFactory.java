package dev.the_fireplace.lib.api.chat.injectables;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;

import java.util.Collection;

public interface TranslatorFactory
{
    /**
     * Properly initializes your translation service.
     * This should happen on both client (If the mod is present there) and server to ensure proper synchronization.
     */
    void addTranslator(String modId);

    Translator getTranslator(String modId);

    Collection<String> availableTranslators();
}
