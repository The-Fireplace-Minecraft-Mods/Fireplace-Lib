package dev.the_fireplace.lib.config;

import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.domain.config.ConfigValues;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.LanguageDefinition;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
@Singleton
public final class FLConfigScreenFactory {
    private static final String TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".option.";

    private final Translator translator;
    private final ConfigStateManager configStateManager;
    private final FLConfig config;
    private final ConfigValues defaultConfigValues;
    private final ConfigScreenBuilderFactory configScreenBuilderFactory;

    private ConfigScreenBuilder configScreenBuilder;

    @Inject
    public FLConfigScreenFactory(
        TranslatorFactory translatorFactory,
        ConfigStateManager configStateManager,
        FLConfig config,
        @Named("default") ConfigValues defaultConfigValues,
        ConfigScreenBuilderFactory configScreenBuilderFactory
    ) {
        this.translator = translatorFactory.getTranslator(FireplaceLib.MODID);
        this.configStateManager = configStateManager;
        this.config = config;
        this.defaultConfigValues = defaultConfigValues;
        this.configScreenBuilderFactory = configScreenBuilderFactory;
    }

    public Screen getConfigScreen(Screen parent) {
        this.configScreenBuilder = configScreenBuilderFactory.create(
            translator,
            TRANSLATION_BASE + "title",
            TRANSLATION_BASE + "global",
            parent,
            () -> configStateManager.save(config)
        );
        addGlobalCategoryEntries();

        return this.configScreenBuilder.build();
    }

    private void addGlobalCategoryEntries() {
        configScreenBuilder.addStringDropdown(
            OPTION_TRANSLATION_BASE + "locale",
            config.getLocale(),
            defaultConfigValues.getLocale(),
            MinecraftClient.getInstance().getLanguageManager().getAllLanguages().parallelStream().map(LanguageDefinition::getCode).collect(Collectors.toList()),
            config::setLocale,
            false
        );
        configScreenBuilder.addShortField(
            OPTION_TRANSLATION_BASE + "essentialThreadPoolSize",
            config.getEssentialThreadPoolSize(),
            defaultConfigValues.getEssentialThreadPoolSize(),
            config::setEssentialThreadPoolSize,
            (short) 1,
            Short.MAX_VALUE
        );
        configScreenBuilder.addShortField(
            OPTION_TRANSLATION_BASE + "nonEssentialThreadPoolSize",
            config.getNonEssentialThreadPoolSize(),
            defaultConfigValues.getNonEssentialThreadPoolSize(),
            config::setNonEssentialThreadPoolSize,
            (short) 1,
            Short.MAX_VALUE
        );
    }
}
