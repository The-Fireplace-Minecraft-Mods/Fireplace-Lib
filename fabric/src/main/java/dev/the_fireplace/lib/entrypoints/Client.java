package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.CompatModids;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.chat.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;
import dev.the_fireplace.lib.config.cloth.FabricClothConfigScreenBuilderFactory;
import dev.the_fireplace.lib.domain.config.ConfigScreenBuilderFactoryProxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class Client implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
        loadConfigScreenBuilder(FireplaceLibConstants.getInjector());
    }

    private void loadConfigScreenBuilder(Injector injector) {
        ConfigScreenBuilderFactory configScreenBuilderFactory = null;
        if (FabricLoader.getInstance().isModLoaded(CompatModids.CLOTH_CONFIG_FABRIC)) {
            configScreenBuilderFactory = injector.getInstance(FabricClothConfigScreenBuilderFactory.class);
        }
        if (configScreenBuilderFactory != null) {
            injector.getInstance(ConfigScreenBuilderFactoryProxy.class).setActiveConfigScreenBuilderFactory(configScreenBuilderFactory);
        }
    }
}
