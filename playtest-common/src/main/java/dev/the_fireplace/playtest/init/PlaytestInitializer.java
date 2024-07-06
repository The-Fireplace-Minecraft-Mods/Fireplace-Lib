package dev.the_fireplace.playtest.init;

import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.environment.injectables.EnvironmentData;
import dev.the_fireplace.playtest.PlaytestConstants;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PlaytestInitializer
{
    private boolean initialized = false;
    private final TranslatorFactory translatorFactory;
    private final EnvironmentData environmentData;

    @Inject
    public PlaytestInitializer(
        TranslatorFactory translatorFactory,
        EnvironmentData environmentData
    ) {
        this.translatorFactory = translatorFactory;
        this.environmentData = environmentData;
    }

    public void init() {
        Logger logger = PlaytestConstants.getLogger();
        if (!initialized) {
            logger.info("Hello world, we have initialized for the first time.");
            initialized = true;
            translatorFactory.addTranslator(PlaytestConstants.MODID);
        }
        logger.info("Init called from the following environment:");
        logger.info("Is client: {}", environmentData.isClient());
        logger.info("Is dedicated server: {}", environmentData.isDedicatedServer());
        logger.info("Is development environment: {}", environmentData.isDevelopment());
    }
}
