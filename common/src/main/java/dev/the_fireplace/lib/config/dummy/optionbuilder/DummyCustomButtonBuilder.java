package dev.the_fireplace.lib.config.dummy.optionbuilder;

import dev.the_fireplace.lib.api.client.interfaces.CustomButtonBuilder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DummyCustomButtonBuilder<S> extends DummyOptionBuilder<S> implements CustomButtonBuilder<S>
{
    @Override
    public CustomButtonBuilder<S> setButtonTextSupplier(@Nullable Function<String, Component> buttonTextSupplier) {
        return this;
    }
}
