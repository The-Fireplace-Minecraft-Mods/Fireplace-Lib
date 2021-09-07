package dev.the_fireplace.lib.compat.modmenu;

import com.google.common.collect.Sets;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.gui.ModsScreen;
import dev.the_fireplace.lib.mixin.clothconfig.AbstractConfigScreenAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Map;

/**
 * Counteract Mod Menu's old caching mechanism (MM 1.16.9 and earlier), which shouldn't be used with Cloth Config GUIs.
 * See also: https://github.com/TerraformersMC/ModMenu/issues/254
 */
@Environment(EnvType.CLIENT)
public final class OldModMenuCompat implements ModMenuCompat {
    @Override
    public void reloadClothConfigGUIs() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof AbstractConfigScreenAccessor) {
            Screen parent = ((AbstractConfigScreenAccessor) screen).getParent();
            if (parent instanceof ModsScreen) {
                Map<String, Screen> configScreenCache = ((ModsScreen) parent).getConfigScreenCache();
                for (Map.Entry<String, Screen> configScreen : Sets.newHashSet(configScreenCache.entrySet())) {
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
