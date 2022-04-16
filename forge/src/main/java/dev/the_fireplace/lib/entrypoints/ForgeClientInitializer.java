package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.lib.CompatModids;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.events.FLEventBus;
import dev.the_fireplace.lib.chat.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;
import dev.the_fireplace.lib.config.ForgeConfigScreenLoader;
import dev.the_fireplace.lib.config.cloth.ForgeClothConfigScreenBuilderFactory;
import dev.the_fireplace.lib.domain.config.ConfigScreenBuilderFactoryProxy;
import dev.the_fireplace.lib.events.ConfigGuiRegistryEventHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ForgeClientInitializer
{
    private final ConfigScreenBuilderFactoryProxy builderFactoryProxy;
    private final ForgeConfigScreenLoader configScreenLoader;
    private final ConfigGuiRegistryEventHandler configGuiRegistryEventHandler;

    @Inject
    public ForgeClientInitializer(
        ConfigScreenBuilderFactoryProxy builderFactoryProxy,
        ForgeConfigScreenLoader configScreenLoader,
        ConfigGuiRegistryEventHandler configGuiRegistryEventHandler
    ) {
        this.builderFactoryProxy = builderFactoryProxy;
        this.configScreenLoader = configScreenLoader;
        this.configGuiRegistryEventHandler = configGuiRegistryEventHandler;
    }

    public void init() {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
        loadConfigScreenBuilder(builderFactoryProxy);

        if (builderFactoryProxy.hasActiveFactory()) {
            FMLJavaModLoadingContext.get().getModEventBus().register(configScreenLoader);
            FLEventBus.BUS.register(configGuiRegistryEventHandler);
        }
    }

    private void loadConfigScreenBuilder(ConfigScreenBuilderFactoryProxy builderFactoryProxy) {
        ConfigScreenBuilderFactory configScreenBuilderFactory = null;
        if (ModList.get().isLoaded(CompatModids.CLOTH_CONFIG_FORGE)) {
            configScreenBuilderFactory = FireplaceLibConstants.getInjector().getInstance(ForgeClothConfigScreenBuilderFactory.class);
        }
        if (configScreenBuilderFactory != null) {
            builderFactoryProxy.setActiveConfigScreenBuilderFactory(configScreenBuilderFactory);
        }
    }
}
