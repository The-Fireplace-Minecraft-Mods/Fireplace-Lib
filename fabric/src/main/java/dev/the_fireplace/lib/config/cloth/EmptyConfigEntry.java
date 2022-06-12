package dev.the_fireplace.lib.config.cloth;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.widget.DynamicEntryListWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class EmptyConfigEntry<E extends DynamicEntryListWidget.Entry<E>> extends AbstractConfigEntry<E>
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
    public Optional<E> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void save() {

    }

    @Override
    public int getItemHeight() {
        return 4;
    }
}
