package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.domain.config.OptionBuilderFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import javax.inject.Inject;

@Implementation
@Environment(EnvType.CLIENT)
public final class ClothConfigScreenBuilderFactory implements ConfigScreenBuilderFactory {
    private final OptionBuilderFactory optionBuilderFactory;

    @Inject
    public ClothConfigScreenBuilderFactory(OptionBuilderFactory optionBuilderFactory) {
        this.optionBuilderFactory = optionBuilderFactory;
    }

    @Override
    public ConfigScreenBuilder create(
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    ) {
        return new ClothConfigScreenBuilder(optionBuilderFactory, translator, titleTranslationKey, initialCategoryTranslationKey, parent, save);
    }
}
