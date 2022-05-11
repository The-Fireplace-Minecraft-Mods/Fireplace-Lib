package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.domain.config.cloth.OptionBuilderFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

import javax.inject.Inject;

@Environment(EnvType.CLIENT)
public final class FabricClothConfigScreenBuilderFactory implements ConfigScreenBuilderFactory
{
    private final OptionBuilderFactory optionBuilderFactory;

    @Inject
    public FabricClothConfigScreenBuilderFactory(OptionBuilderFactory optionBuilderFactory) {
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
        return new FabricClothConfigScreenBuilder(optionBuilderFactory, translator, titleTranslationKey, initialCategoryTranslationKey, parent, save);
    }
}
