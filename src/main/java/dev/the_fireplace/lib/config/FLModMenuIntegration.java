package dev.the_fireplace.lib.config;

import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.domain.config.ConfigValues;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Singleton
public final class FLModMenuIntegration implements ModMenuApi {
    private static final String TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".option.";

    private final Translator translator;
    private final ConfigStateManager configStateManager;
    private final FLConfig config;
    private final ConfigValues defaultConfigValues;
    private final ConfigScreenBuilderFactory configScreenBuilderFactory;

    private ConfigScreenBuilder configScreenBuilder;

    @Inject
    public FLModMenuIntegration(
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

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> {
            this.configScreenBuilder = configScreenBuilderFactory.create(
                translator,
                TRANSLATION_BASE + "title",
                TRANSLATION_BASE + "global",
                parent,
                () -> configStateManager.save(config)
            );
            addGlobalCategoryEntries();

            return this.configScreenBuilder.build();
        };
    }

    private void addGlobalCategoryEntries() {
        configScreenBuilder.addStringField(
            OPTION_TRANSLATION_BASE + "locale",
            config.getLocale(),
            defaultConfigValues.getLocale(),
            config::setLocale
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

        if (FireplaceLib.isDevelopmentEnvironment()) {
            configScreenBuilder.addBoolToggle(
                OPTION_TRANSLATION_BASE + "showSecretOptions",
                false,
                false,
                (val) -> {}
            );
            configScreenBuilder.addIntSlider(
                OPTION_TRANSLATION_BASE + "ligma",
                0,
                2,
                (val) -> {},
                0,
                20
            );
            configScreenBuilder.addFloatSlider(
                OPTION_TRANSLATION_BASE + "float",
                0,
                1,
                (val) -> {},
                0,
                20
            );
            configScreenBuilder.addByteSlider(
                OPTION_TRANSLATION_BASE + "bite",
                (byte)0,
                (byte)1,
                (val) -> {},
                (byte)0,
                (byte)20
            );
            configScreenBuilder.addOptionDependency(
                OPTION_TRANSLATION_BASE + "showSecretOptions",
                OPTION_TRANSLATION_BASE + "ligma",
                (parentValue) -> (boolean) parentValue
            );
            configScreenBuilder.addOptionDependency(
                OPTION_TRANSLATION_BASE + "ligma",
                OPTION_TRANSLATION_BASE + "float",
                (parentValue) -> ((int) parentValue) > 10
            );
            configScreenBuilder.addOptionDependency(
                OPTION_TRANSLATION_BASE + "float",
                OPTION_TRANSLATION_BASE + "bite",
                (parentValue) -> ((float) parentValue) > 6.9
            );
        }
    }
}
