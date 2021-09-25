package dev.the_fireplace.lib.api.client.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import javax.annotation.Nullable;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public interface CustomButtonBuilder<S> extends OptionBuilder<S> {
    CustomButtonBuilder<S> setButtonTextSupplier(@Nullable Function<String, Text> buttonTextSupplier);
}
