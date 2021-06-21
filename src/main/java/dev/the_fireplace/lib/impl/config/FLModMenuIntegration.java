package dev.the_fireplace.lib.impl.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.impl.FireplaceLib;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public final class FLModMenuIntegration implements ModMenuApi {
    private static final String TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = "text.config." + FireplaceLib.MODID + ".option.";

    private final FLConfig.Access defaultData = FLConfig.getDefaultData();
    private final FLConfig.Access configAccess = FLConfig.getData();
    
    private final Translator translator;
    private final ConfigStateManager configStateManager;
    
    private ConfigScreenBuilder configScreenBuilder;

    public FLModMenuIntegration() {
        this.translator = DIContainer.get().getInstance(TranslatorFactory.class).getTranslator(FireplaceLib.MODID);
        this.configStateManager = DIContainer.get().getInstance(ConfigStateManager.class);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText(TRANSLATION_BASE + "title"));

            buildConfigCategories(builder);

            builder.setSavingRunnable(() -> configStateManager.save(FLConfig.getInstance()));
            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(new TranslatableText(TRANSLATION_BASE + "global"));
        global.setDescription(new StringVisitable[]{new TranslatableText(TRANSLATION_BASE + "global.desc")});
        this.configScreenBuilder = DIContainer.get().getInstance(ConfigScreenBuilderFactory.class).create(translator, entryBuilder, global);
        addGlobalCategoryEntries();
    }

    private void addGlobalCategoryEntries() {
        configScreenBuilder.addStringField(
            OPTION_TRANSLATION_BASE + "locale",
            configAccess.getLocale(),
            defaultData.getLocale(),
            configAccess::setLocale
        );
        configScreenBuilder.addShortField(
            OPTION_TRANSLATION_BASE + "essentialThreadPoolSize",
            configAccess.getEssentialThreadPoolSize(),
            defaultData.getEssentialThreadPoolSize(),
            configAccess::setEssentialThreadPoolSize,
            (short) 1,
            Short.MAX_VALUE
        );
        configScreenBuilder.addShortField(
            OPTION_TRANSLATION_BASE + "nonEssentialThreadPoolSize",
            configAccess.getNonEssentialThreadPoolSize(),
            defaultData.getNonEssentialThreadPoolSize(),
            configAccess::setNonEssentialThreadPoolSize,
            (short) 1,
            Short.MAX_VALUE
        );
    }
}
