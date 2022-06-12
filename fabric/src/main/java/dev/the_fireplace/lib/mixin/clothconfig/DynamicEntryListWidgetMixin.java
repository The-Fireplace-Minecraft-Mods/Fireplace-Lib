package dev.the_fireplace.lib.mixin.clothconfig;

import dev.the_fireplace.lib.config.cloth.ClothConfigDependencyHandler;
import dev.the_fireplace.lib.config.cloth.EmptyConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.widget.DynamicEntryListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@Environment(EnvType.CLIENT)
@Mixin(value = DynamicEntryListWidget.class, remap = false)
public abstract class DynamicEntryListWidgetMixin<E extends DynamicEntryListWidget.Entry<E>> extends AbstractContainerEventHandler
{
    @Shadow
    public abstract List<E> children();

    @Shadow
    @Final
    private List<E> entries;
    private final Map<Integer, AbstractConfigEntry<?>> disabledEntries = new HashMap<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void swapEntriesBeforeRender(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof ClothConfigScreen.ListWidget)) {
            return;
        }
        List<GuiEventListener> children = new ArrayList<>(this.children());
        for (int index = 0; index < children.size(); index++) {
            if (disabledEntries.containsKey(index) && !ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(disabledEntries.get(index))) {
                AbstractConfigEntry<?> configEntry = this.disabledEntries.remove(index);
                this.entries.set(index, (E) configEntry);
            } else if (!disabledEntries.containsKey(index)
                && children.get(index) instanceof AbstractConfigEntry<?>
                && ClothConfigDependencyHandler.DISABLED_ENTRIES.contains((AbstractConfigEntry<?>) children.get(index))
            ) {
                this.disabledEntries.put(index, (AbstractConfigEntry<?>) children.get(index));
                this.entries.set(index, (E) new EmptyConfigEntry<E>());
            }
        }
    }
}
