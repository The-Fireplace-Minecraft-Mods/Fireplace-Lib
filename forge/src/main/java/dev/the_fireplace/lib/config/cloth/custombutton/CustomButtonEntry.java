package dev.the_fireplace.lib.config.cloth.custombutton;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreen;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreenFactory;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.config.cloth.ClothConfigDependencyHandler;
import io.netty.util.concurrent.Promise;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CustomButtonEntry extends TooltipListEntry<String>
{
    private final AtomicReference<String> value;
    private final String original;
    private final Button buttonWidget;
    private final Button resetButton;
    private final Consumer<String> saveConsumer;
    private final Supplier<String> defaultValue;
    private final List<AbstractWidget> widgets;
    private final Function<String, Component> getDisplayText;

    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public CustomButtonEntry(
        Component fieldName,
        String currentValue,
        Component resetButtonKey,
        Supplier<String> defaultValue,
        Consumer<String> saveConsumer,
        CustomButtonScreenFactory<String, ?> buildOptionScreenFactory,
        Function<String, Component> getDisplayText
    ) {
        super(fieldName, null);
        this.defaultValue = defaultValue;
        this.original = currentValue;
        this.value = new AtomicReference<>(currentValue);
        this.buttonWidget = new Button(0, 0, 150, 20, Component.empty(), (widget) -> {
            Screen optionBuilderScreen = buildOptionScreenFactory.createScreen(Minecraft.getInstance().screen, this.value.get());
            //noinspection unchecked
            Promise<Optional<String>> willReturnNewValuePromise = ((CustomButtonScreen<String>) optionBuilderScreen).getNewValuePromise();
            Minecraft.getInstance().setScreen(optionBuilderScreen);
            FireplaceLibConstants.getInjector().getInstance(ExecutionManager.class).runKillable(() -> {
                Optional<String> builderReturnedValue = Optional.empty();
                try {
                    builderReturnedValue = willReturnNewValuePromise.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                builderReturnedValue.ifPresent(this.value::set);
            });
        });
        this.resetButton = new Button(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20, resetButtonKey, (widget) -> {
            this.value.set(defaultValue.get());
        });
        this.saveConsumer = saveConsumer;
        this.widgets = Lists.newArrayList(new AbstractWidget[]{this.buttonWidget, this.resetButton});
        this.getDisplayText = getDisplayText;
    }

    @Override
    public boolean isEdited() {
        return super.isEdited() || !Objects.equals(this.original, this.value.get());
    }

    @Override
    public void save() {
        if (this.saveConsumer != null) {
            this.saveConsumer.accept(this.getValue());
        }
    }

    @Override
    public String getValue() {
        return this.value.get();
    }

    @Override
    public Optional<String> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable(this.defaultValue.get());
    }

    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        if (ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(this)) {
            return;
        }
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && !Objects.equals(this.defaultValue.get(), this.value.get());
        this.resetButton.y = y;
        this.buttonWidget.active = this.isEditable();
        this.buttonWidget.y = y;
        Component buttonText = getDisplayText != null ? getDisplayText.apply(this.value.get()) : Component.nullToEmpty(this.value.get());
        this.buttonWidget.setMessage(buttonText);
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            Minecraft.getInstance().font.drawShadow(matrices, displayedFieldName.getVisualOrderText(), (float) (window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName)), (float) (y + 6), 0xFFFFFF);
            this.resetButton.x = x;
            this.buttonWidget.x = x + this.resetButton.getWidth() + 2;
        } else {
            Minecraft.getInstance().font.drawShadow(matrices, displayedFieldName.getVisualOrderText(), (float) x, (float) (y + 6), this.getPreferredTextColor());
            this.resetButton.x = x + entryWidth - this.resetButton.getWidth();
            this.buttonWidget.x = x + entryWidth - 150;
        }

        this.buttonWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.render(matrices, mouseX, mouseY, delta);
        this.buttonWidget.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return widgets;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return widgets;
    }
}
