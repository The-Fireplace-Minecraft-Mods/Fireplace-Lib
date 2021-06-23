package dev.the_fireplace.lib.entrypoints;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.config.FLModMenuIntegration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ModMenuEntrypoint implements ModMenuApi {
    private final FLModMenuIntegration flModMenuIntegration = DIContainer.get().getInstance(FLModMenuIntegration.class);

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return flModMenuIntegration.getModConfigScreenFactory();
    }
}
