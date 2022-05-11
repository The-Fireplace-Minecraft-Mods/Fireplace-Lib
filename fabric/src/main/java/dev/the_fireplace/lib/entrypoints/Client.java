package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.entrypoints.ClientDIModInitializer;
import dev.the_fireplace.lib.CompatModids;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.chat.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;
import dev.the_fireplace.lib.config.ConfigScreenBuilderProxy;
import dev.the_fireplace.lib.config.cloth.FabricClothConfigScreenBuilderFactory;
import net.fabricmc.loader.api.FabricLoader;

public final class Client implements ClientDIModInitializer
{
    @Override
    public void onInitializeClient(Injector injector) {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
        loadConfigScreenBuilder(injector);
    }

    private void loadConfigScreenBuilder(Injector injector) {
        ConfigScreenBuilderFactory configScreenBuilderFactory = null;
        if (FabricLoader.getInstance().isModLoaded(CompatModids.CLOTH_CONFIG_FABRIC)) {
            configScreenBuilderFactory = injector.getInstance(FabricClothConfigScreenBuilderFactory.class);
        }
        if (configScreenBuilderFactory != null) {
            injector.getInstance(ConfigScreenBuilderProxy.class).setActiveConfigScreenBuilderFactory(configScreenBuilderFactory);
        }
    }
}
