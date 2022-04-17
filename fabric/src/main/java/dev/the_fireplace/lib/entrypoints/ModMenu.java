package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.client.entrypoints.ConfigGuiEntrypoint;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class ModMenu implements ModMenuApi
{
    private final FLConfigScreenFactory flConfigScreenFactory = DIContainer.get().getInstance(FLConfigScreenFactory.class);
    private Map<String, ConfigScreenFactory<?>> configScreenFactories = null;

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        if (this.configScreenFactories == null) {
            loadConfigScreenFactories();
        }

        return this.configScreenFactories;
    }

    private void loadConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> configScreenFactories = new HashMap<>();
        ModMenuConfigGuiRegistry configGuiRegistry = new ModMenuConfigGuiRegistry(configScreenFactories);
        Injector injector = DIContainer.get();

        FabricLoader.getInstance().getEntrypointContainers("fireplacelib", ConfigGuiEntrypoint.class).forEach(
            configGuiEntrypoint -> {
                configGuiRegistry.activeModId = configGuiEntrypoint.getProvider().getMetadata().getId();
                configGuiEntrypoint.getEntrypoint().registerConfigGuis(injector, configGuiRegistry);
            }
        );

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
        public <S extends Screen> void register(dev.the_fireplace.lib.api.client.interfaces.ConfigScreenFactory<S> createConfigGui) {
            configScreenFactories.put(activeModId, createConfigGui::create);
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // This is only necessary because Mod Menu detects this entrypoint and won't use getProvidedConfigScreenFactories for this mod's config GUI when it does.
        // Mods using FL's entrypoint shouldn't need a mod menu entrypoint at all.
        return (ConfigScreenFactory<Screen>) flConfigScreenFactory::getConfigScreen;
    }
}
