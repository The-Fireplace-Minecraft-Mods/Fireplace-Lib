package dev.the_fireplace.lib.config.cloth.custombutton;

import com.google.common.collect.Lists;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreen;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreenFactory;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import io.netty.util.concurrent.Promise;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class CustomButtonEntry extends TooltipListEntry<String> {
    private final AtomicReference<String> value;
    private final String original;
    private final ButtonWidget buttonWidget;
    private final ButtonWidget resetButton;
    private final Consumer<String> saveConsumer;
    private final Supplier<String> defaultValue;
    private final List<ClickableWidget> widgets;
    private final Function<String, Text> getDisplayText;

    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public CustomButtonEntry(
        Text fieldName,
        String currentValue,
        Text resetButtonKey,
        Supplier<String> defaultValue,
        Consumer<String> saveConsumer,
        CustomButtonScreenFactory<String, ?> buildOptionScreenFactory,
        Function<String, Text> getDisplayText
    ) {
        super(fieldName, null);
        this.defaultValue = defaultValue;
        this.original = currentValue;
        this.value = new AtomicReference<>(currentValue);
        this.buttonWidget = new ButtonWidget(0, 0, 150, 20, NarratorManager.EMPTY, (widget) -> {
            Screen optionBuilderScreen = buildOptionScreenFactory.createScreen(MinecraftClient.getInstance().currentScreen, this.value.get());
            //noinspection unchecked
            Promise<Optional<String>> willReturnNewValuePromise = ((CustomButtonScreen<String>) optionBuilderScreen).getNewValuePromise();
            MinecraftClient.getInstance().openScreen(optionBuilderScreen);
            DIContainer.get().getInstance(ExecutionManager.class).runKillable(() -> {
                Optional<String> builderReturnedValue = Optional.empty();
                try {
                    builderReturnedValue = willReturnNewValuePromise.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                builderReturnedValue.ifPresent(this.value::set);
            });
        });
        this.resetButton = new ButtonWidget(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(resetButtonKey) + 6, 20, resetButtonKey, (widget) -> {
            this.value.set(defaultValue.get());
        });
        this.saveConsumer = saveConsumer;
        this.widgets = Lists.newArrayList(new ClickableWidget[]{this.buttonWidget, this.resetButton});
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
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = MinecraftClient.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && !Objects.equals(this.defaultValue.get(), this.value.get());
        this.resetButton.y = y;
        this.buttonWidget.active = this.isEditable();
        this.buttonWidget.y = y;
        Text buttonText = getDisplayText != null ? getDisplayText.apply(this.value.get()) : Text.of(this.value.get());
        this.buttonWidget.setMessage(buttonText);
        Text displayedFieldName = this.getDisplayedFieldName();
        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, displayedFieldName.asOrderedText(), (float) (window.getScaledWidth() - x - MinecraftClient.getInstance().textRenderer.getWidth(displayedFieldName)), (float) (y + 6), 0xFFFFFF);
            this.resetButton.x = x;
            this.buttonWidget.x = x + this.resetButton.getWidth() + 2;
        } else {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, displayedFieldName.asOrderedText(), (float) x, (float) (y + 6), this.getPreferredTextColor());
            this.resetButton.x = x + entryWidth - this.resetButton.getWidth();
            this.buttonWidget.x = x + entryWidth - 150;
        }

        this.buttonWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.render(matrices, mouseX, mouseY, delta);
        this.buttonWidget.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public List<? extends Selectable> narratables() {
        return widgets;
    }

    @Override
    public List<? extends Element> children() {
        return widgets;
    }
}
