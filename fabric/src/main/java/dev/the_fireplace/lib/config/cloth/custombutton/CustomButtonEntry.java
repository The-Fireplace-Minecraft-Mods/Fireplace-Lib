package dev.the_fireplace.lib.config.cloth.custombutton;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreen;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreenFactory;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.config.cloth.ClothConfigDependencyHandler;
import io.netty.util.concurrent.Promise;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class CustomButtonEntry extends TooltipListEntry<String>
{
    private final AtomicReference<String> value;
    private final Button buttonWidget;
    private final Button resetButton;
    private final Consumer<String> saveConsumer;
    private final Supplier<String> defaultValue;
    private final List<AbstractWidget> widgets;
    private final Function<String, String> getDisplayString;

    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public CustomButtonEntry(
        String fieldName,
        String currentValue,
        String resetButtonKey,
        Supplier<String> defaultValue,
        Consumer<String> saveConsumer,
        CustomButtonScreenFactory<String, ?> buildOptionScreenFactory,
        Function<String, String> getDisplayString
    ) {
        super(fieldName, null);
        this.defaultValue = defaultValue;
        this.value = new AtomicReference<>(currentValue);
        this.buttonWidget = new Button(0, 0, 150, 20, "", (widget) -> {
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
                getScreen().setEdited(true, isRequiresRestart());
            });
        });
        String resetButtonText = I18n.get(resetButtonKey);
        this.resetButton = new Button(0, 0, Minecraft.getInstance().font.width(resetButtonText) + 6, 20, resetButtonText, (widget) -> {
            this.value.set(defaultValue.get());
            getScreen().setEdited(true, isRequiresRestart());
        });
        this.saveConsumer = saveConsumer;
        this.widgets = Lists.newArrayList(this.buttonWidget, this.resetButton);
        this.getDisplayString = getDisplayString;
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
    public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        if (ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(this)) {
            return;
        }
        super.render(index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && !Objects.equals(this.defaultValue.get(), this.value.get());
        this.resetButton.y = y;
        this.buttonWidget.active = this.isEditable();
        this.buttonWidget.y = y;
        String buttonText = getDisplayString != null ? getDisplayString.apply(this.value.get()) : this.value.get();
        this.buttonWidget.setMessage(buttonText);
        String displayedFieldName = I18n.get(this.getFieldName());
        if (Minecraft.getInstance().font.isBidirectional()) {
            Minecraft.getInstance().font.drawShadow(displayedFieldName, (float) (window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName)), (float) (y + 6), 0xFFFFFF);
            this.resetButton.x = x;
            this.buttonWidget.x = x + this.resetButton.getWidth() + 2;
        } else {
            Minecraft.getInstance().font.drawShadow(displayedFieldName, (float) x, (float) (y + 6), this.getPreferredTextColor());
            this.resetButton.x = x + entryWidth - this.resetButton.getWidth();
            this.buttonWidget.x = x + entryWidth - 150;
        }

        this.buttonWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.render(mouseX, mouseY, delta);
        this.buttonWidget.render(mouseX, mouseY, delta);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return widgets;
    }
}
