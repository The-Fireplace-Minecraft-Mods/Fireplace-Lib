package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Client side only
 */
public interface CustomButtonBuilder<S> extends OptionBuilder<S>
{
    CustomButtonBuilder<S> setButtonTextSupplier(@Nullable Function<String, Component> buttonTextSupplier);
}
