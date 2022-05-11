package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.domain.config.cloth.OptionBuilderFactory;
import net.minecraft.client.gui.screens.Screen;

import javax.inject.Inject;
import java.util.Optional;

public final class ForgeClothConfigScreenBuilderFactory implements ConfigScreenBuilderFactory
{
    private final OptionBuilderFactory optionBuilderFactory;

    @Inject
    public ForgeClothConfigScreenBuilderFactory(OptionBuilderFactory optionBuilderFactory) {
        this.optionBuilderFactory = optionBuilderFactory;
    }

    @Override
    public Optional<ConfigScreenBuilder> create(
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    ) {
        return Optional.of(new ForgeClothConfigScreenBuilder(optionBuilderFactory, translator, titleTranslationKey, initialCategoryTranslationKey, parent, save));
    }
}
