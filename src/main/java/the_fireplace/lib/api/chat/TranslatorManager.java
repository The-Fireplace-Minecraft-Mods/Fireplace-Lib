package the_fireplace.lib.api.chat;

import the_fireplace.lib.impl.translation.TranslatorManagerImpl;

import java.util.Collection;

public interface TranslatorManager {
    static TranslatorManager getInstance() {
        //noinspection deprecation
        return TranslatorManagerImpl.INSTANCE;
    }
    /**
     * Properly initializes your translation service.
     * This should happen on both client (If the mod is present there) and server to ensure proper synchronization.
     * {@link net.fabricmc.api.ModInitializer#onInitialize} is a good place to do so.
     */
    void addTranslator(String modid);

    Translator getTranslator(String modid);

    Collection<String> availableTranslators();
}
