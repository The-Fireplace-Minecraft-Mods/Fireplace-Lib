package dev.the_fireplace.lib.mixin.clothconfig;

import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(value = AbstractConfigEntry.class, remap = false)
public interface AbstractConfigEntryAccessor<T> {
    @Accessor
    Supplier<Optional<Text>> getErrorSupplier();
}
