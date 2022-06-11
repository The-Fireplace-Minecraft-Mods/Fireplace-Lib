package dev.the_fireplace.lib.config.cloth.custombutton;

import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreenFactory;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CustomButtonFieldBuilder extends FieldBuilder<String, CustomButtonEntry>
{
    @Nullable
    private Consumer<String> saveConsumer = null;

    private Function<String, Optional<Component[]>> tooltipSupplier = (value) -> Optional.empty();

    private final String value;

    @Nullable
    private Function<String, Component> buttonTextSupplier = null;

    private final CustomButtonScreenFactory<String, ?> buildOptionScreenFactory;

    public CustomButtonFieldBuilder(Component resetButtonKey, Component fieldNameKey, String value, CustomButtonScreenFactory<String, ?> buildOptionScreenFactory) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
        this.buildOptionScreenFactory = buildOptionScreenFactory;
    }

    public CustomButtonFieldBuilder setErrorSupplier(@Nullable Function<String, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }

    public CustomButtonFieldBuilder requireRestart() {
        this.requireRestart(true);
        return this;
    }

    public CustomButtonFieldBuilder setSaveConsumer(Consumer<String> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public CustomButtonFieldBuilder setDefaultValue(Supplier<String> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public CustomButtonFieldBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public CustomButtonFieldBuilder setTooltipSupplier(Function<String, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    public CustomButtonFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = (value) -> tooltipSupplier.get();
        return this;
    }

    public CustomButtonFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = (bool) -> tooltip;
        return this;
    }

    public CustomButtonFieldBuilder setTooltip(@Nullable Component... tooltip) {
        this.tooltipSupplier = (bool) -> Optional.ofNullable(tooltip);
        return this;
    }

    @Nullable
    public Function<String, Component> getButtonTextSupplier() {
        return this.buttonTextSupplier;
    }

    public CustomButtonFieldBuilder setButtonTextSupplier(@Nullable Function<String, Component> buttonTextSupplier) {
        this.buttonTextSupplier = buttonTextSupplier;
        return this;
    }

    @Override
    public CustomButtonEntry build() {
        CustomButtonEntry entry = new CustomButtonEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.saveConsumer, this.buildOptionScreenFactory, this.buttonTextSupplier);
        entry.setTooltipSupplier(() -> this.tooltipSupplier.apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> this.errorSupplier.apply(entry.getValue()));
        }
        entry.setRequiresRestart(this.requireRestart);

        return entry;
    }
}
