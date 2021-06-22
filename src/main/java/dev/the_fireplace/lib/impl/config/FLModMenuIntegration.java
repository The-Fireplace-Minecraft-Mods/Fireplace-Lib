package dev.the_fireplace.lib.impl.config;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.impl.FireplaceLib;
import dev.the_fireplace.lib.impl.domain.config.ConfigValues;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
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
    
    private ConfigScreenBuilder configScreenBuilder;

    @Inject
    public FLModMenuIntegration(
        TranslatorFactory translatorFactory,
        ConfigStateManager configStateManager,
        FLConfig config,
        @Named("default") ConfigValues defaultConfigValues
    ) {
        this.translator = translatorFactory.getTranslator(FireplaceLib.MODID);
        this.configStateManager = DIContainer.get().getInstance(ConfigStateManager.class);
        this.config = config;
        this.defaultConfigValues = defaultConfigValues;
    }

    @Override
    public String getModId() {
        return FireplaceLib.MODID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(translator.getTranslatedString(TRANSLATION_BASE + "title"));

            buildConfigCategories(builder);

            builder.setSavingRunnable(() -> configStateManager.save(config));

            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(translator.getTranslatedString(TRANSLATION_BASE + "global"));
        this.configScreenBuilder = DIContainer.get().getInstance(ConfigScreenBuilderFactory.class).create(translator, entryBuilder, global);
        addGlobalCategoryEntries();
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
    }
}
