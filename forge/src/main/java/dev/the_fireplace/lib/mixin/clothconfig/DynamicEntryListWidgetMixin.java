package dev.the_fireplace.lib.mixin.clothconfig;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.lib.config.cloth.ClothConfigDependencyHandler;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.widget.DynamicEntryListWidget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
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
    private void swapEntriesBeforeRender(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof ClothConfigScreen.ListWidget)) {
            return;
        }
        List<GuiEventListener> children = new ArrayList<>(this.children());
        for (int index = 0; index < children.size(); index++) {
            if (disabledEntries.containsKey(index) && !ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(disabledEntries.get(index))) {
                AbstractConfigEntry<?> configEntry = this.disabledEntries.remove(index);
                this.entries.set(index, (E) configEntry);
            } else if (!disabledEntries.containsKey(index)
                && children.get(index) instanceof AbstractConfigEntry<?> configEntry
                && ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(configEntry)
            ) {
                this.disabledEntries.put(index, configEntry);
                this.entries.set(index, (E) createEmptyEntry());
            }
        }
    }

    private AbstractConfigEntry<E> createEmptyEntry() {
        return new AbstractConfigEntry<>()
        {
            @Override
            public List<? extends GuiEventListener> children() {
                return Collections.emptyList();
            }

            @Override
            public void render(PoseStack matrixStack, int i, int i1, int i2, int i3, int i4, int i5, int i6, boolean b, float v) {

            }

            @Override
            public boolean isRequiresRestart() {
                return false;
            }

            @Override
            public void setRequiresRestart(boolean b) {

            }

            @Override
            public Component getFieldName() {
                return Component.nullToEmpty("");
            }

            @Override
            public E getValue() {
                return null;
            }

            @Override
            public Optional getDefaultValue() {
                return Optional.empty();
            }

            @Override
            public void save() {

            }

            @Override
            public int getItemHeight() {
                return 4;
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return Collections.emptyList();
            }
        };
    }
}
