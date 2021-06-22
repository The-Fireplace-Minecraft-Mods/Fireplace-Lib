package dev.the_fireplace.lib.impl.thirdpartyentrypoints;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.impl.config.FLModMenuIntegration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuEntrypoint implements ModMenuApi {
    private final FLModMenuIntegration flModMenuIntegration = DIContainer.get().getInstance(FLModMenuIntegration.class);

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return flModMenuIntegration.getModConfigScreenFactory();
    }
}
