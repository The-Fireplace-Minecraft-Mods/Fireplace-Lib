package dev.the_fireplace.lib.config.cloth;

import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.widget.DynamicEntryListWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;

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
    public boolean isRequiresRestart() {
        return false;
    }

    @Override
    public void setRequiresRestart(boolean b) {

    }

    @Override
    public String getFieldName() {
        return "";
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
    public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {

    }

    @Override
    public int getItemHeight() {
        return 4;
    }
}
