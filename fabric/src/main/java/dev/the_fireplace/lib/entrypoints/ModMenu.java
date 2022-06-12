package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.client.entrypoints.ConfigGuiEntrypoint;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;
import dev.the_fireplace.lib.domain.config.ConfigScreenBuilderFactoryProxy;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public final class ModMenu implements ModMenuApi
{
    private final FLConfigScreenFactory flConfigScreenFactory = FireplaceLibConstants.getInjector().getInstance(FLConfigScreenFactory.class);
    private Map<String, ConfigScreenFactory<?>> configScreenFactories = null;

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        if (this.configScreenFactories == null) {
            if (hasOldModMenu()) {
                return new HashMap<>();
            } else {
                loadConfigScreenFactories();
            }
        }

        return this.configScreenFactories;
    }

    private void loadConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> configScreenFactories = new HashMap<>();
        ModMenuConfigGuiRegistry configGuiRegistry = new ModMenuConfigGuiRegistry(configScreenFactories);
        Injector injector = FireplaceLibConstants.getInjector();
        ConfigScreenBuilderFactoryProxy builderFactoryProxy = injector.getInstance(ConfigScreenBuilderFactoryProxy.class);
        if (builderFactoryProxy.hasActiveFactory()) {
            FabricLoader.getInstance().getEntrypointContainers("fireplacelib", ConfigGuiEntrypoint.class).forEach(
                configGuiEntrypoint -> {
                    configGuiRegistry.activeModId = configGuiEntrypoint.getProvider().getMetadata().getId();
                    configGuiEntrypoint.getEntrypoint().registerConfigGuis(injector, configGuiRegistry);
                }
            );
        }

        this.configScreenFactories = configGuiRegistry.configScreenFactories;
    }

    private static class ModMenuConfigGuiRegistry implements ConfigGuiRegistry
    {
        private final Map<String, ConfigScreenFactory<?>> configScreenFactories;
        private String activeModId;

        public ModMenuConfigGuiRegistry(Map<String, ConfigScreenFactory<?>> configScreenFactories) {
            this.configScreenFactories = configScreenFactories;
        }

        @Override
        public <S extends Screen> void register(String modid, dev.the_fireplace.lib.api.client.interfaces.ConfigScreenFactory<S> createConfigGui) {
            if (!Objects.equals(activeModId, modid)) {
                FireplaceLibConstants.getLogger().warn("Registering config screen for mod {} while active mod is {}.", modid, activeModId);
            }
            configScreenFactories.put(modid, createConfigGui::create);
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (hasOldModMenu()) {
            return ModMenuApi.super.getModConfigScreenFactory();
        }
        // This is only necessary because Mod Menu detects this entrypoint and won't use getProvidedConfigScreenFactories for this mod's config GUI when it does.
        // Mods using FL's entrypoint shouldn't need a mod menu entrypoint at all.
        Injector injector = FireplaceLibConstants.getInjector();
        ConfigScreenBuilderFactoryProxy builderFactoryProxy = injector.getInstance(ConfigScreenBuilderFactoryProxy.class);
        if (builderFactoryProxy.hasActiveFactory()) {
            return (ConfigScreenFactory<Screen>) flConfigScreenFactory::getConfigScreen;
        } else {
            return ModMenuApi.super.getModConfigScreenFactory();
        }
    }

    private boolean hasOldModMenu() {
        Optional<ModContainer> modmenu = FabricLoader.getInstance().getModContainer("modmenu");
        try {
            if (modmenu.isPresent()) {
                SemanticVersion modMenuVersion = SemanticVersion.parse(modmenu.get().getMetadata().getVersion().getFriendlyString());
                return modMenuVersion.compareTo(SemanticVersion.parse("1.16.9")) < 1;
            }
        } catch (VersionParsingException e) {
            FireplaceLibConstants.getLogger().error("Unable to parse mod menu version", e);
        }

        return false;
    }
}
