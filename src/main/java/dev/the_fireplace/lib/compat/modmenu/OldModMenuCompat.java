package dev.the_fireplace.lib.compat.modmenu;

import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.gui.ModsScreen;
import dev.the_fireplace.lib.mixin.clothconfig.AbstractConfigScreenAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Map;
import java.util.Set;

/**
 * Counteract Mod Menu's old caching mechanism (MM 2.0.2 and earlier), which shouldn't be used with Cloth Config GUIs.
 * See also: https://github.com/TerraformersMC/ModMenu/issues/254
 */
@Environment(EnvType.CLIENT)
public final class OldModMenuCompat implements ModMenuCompat {
    @Override
    public void reloadClothConfigGUIs() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof AbstractConfigScreenAccessor configScreenAccessor) {
            Screen parent = configScreenAccessor.getParent();
            if (parent instanceof ModsScreen modsScreen) {
                Map<String, Screen> configScreenCache = modsScreen.getConfigScreenCache();
                for (Map.Entry<String, Screen> configScreen : Set.copyOf(configScreenCache.entrySet())) {
                    boolean isClothConfigScreen = configScreen.getValue() instanceof AbstractConfigScreenAccessor;
                    if (!isClothConfigScreen) {
                        continue;
                    }
                    String modid = configScreen.getKey();
                    Screen rebuiltScreen = ModMenu.getConfigScreen(modid, parent);
                    configScreenCache.put(modid, rebuiltScreen);
                }
            }
        }
    }
}
