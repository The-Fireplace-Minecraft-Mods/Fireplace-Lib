package dev.the_fireplace.lib.impl.config;

import dev.the_fireplace.lib.api.chat.TranslatorManager;
import dev.the_fireplace.lib.api.client.ConfigScreenBuilder;
import dev.the_fireplace.lib.impl.FireplaceLib;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class FLModMenuIntegration extends ConfigScreenBuilder implements ModMenuApi {
    private static final String TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".option.";

    private final FLConfig.Access defaultData = FLConfig.getDefaultData();
    private final FLConfig.Access configAccess = FLConfig.getData();

    public FLModMenuIntegration() {
        super(TranslatorManager.getInstance().getTranslator(FireplaceLib.MODID));
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

            builder.setSavingRunnable(() -> FLConfig.getInstance().save());
            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(translator.getTranslatedString(TRANSLATION_BASE + "global"));
        addGlobalCategoryEntries(entryBuilder, global);
    }

    private void addGlobalCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory global) {
        addStringField(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "locale",
            configAccess.getLocale(),
            defaultData.getLocale(),
            configAccess::setLocale
        );
        addShortField(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "essentialThreadPoolSize",
            configAccess.getEssentialThreadPoolSize(),
            defaultData.getEssentialThreadPoolSize(),
            configAccess::setEssentialThreadPoolSize,
            (short) 1,
            Short.MAX_VALUE
        );
        addShortField(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "nonEssentialThreadPoolSize",
            configAccess.getNonEssentialThreadPoolSize(),
            defaultData.getNonEssentialThreadPoolSize(),
            configAccess::setNonEssentialThreadPoolSize,
            (short) 1,
            Short.MAX_VALUE
        );
    }
}
