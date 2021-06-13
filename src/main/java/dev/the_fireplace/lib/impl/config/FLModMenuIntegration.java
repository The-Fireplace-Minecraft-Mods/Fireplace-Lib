package dev.the_fireplace.lib.impl.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.annotateddi.AnnotatedDI;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.client.lib.ConfigScreenBuilder;
import dev.the_fireplace.lib.impl.FireplaceLib;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public final class FLModMenuIntegration extends ConfigScreenBuilder implements ModMenuApi {
    private static final String TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".option.";

    private final FLConfig.Access defaultData = FLConfig.getDefaultData();
    private final FLConfig.Access configAccess = FLConfig.getData();

    public FLModMenuIntegration() {
        super(AnnotatedDI.getInjector().getInstance(TranslatorFactory.class).getTranslator(FireplaceLib.MODID));
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText(TRANSLATION_BASE + "title"));

            buildConfigCategories(builder);

            builder.setSavingRunnable(() -> FLConfig.getInstance().save());
            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(new TranslatableText(TRANSLATION_BASE + "global"));
        global.setDescription(new StringVisitable[]{new TranslatableText(TRANSLATION_BASE + "global.desc")});
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
