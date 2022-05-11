package dev.the_fireplace.lib.api.client.injectables;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;

/**
 * Client side only.
 */
public interface ConfigScreenBuilderFactory
{
    /**
     * Create a new config screen builder.
     * If no supported config GUI mods are available, returns empty.
     * Should never be empty when creating from the Config GUI entrypoint (Fabric) or event (Forge).
     */
    Optional<ConfigScreenBuilder> create(
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    );
}
