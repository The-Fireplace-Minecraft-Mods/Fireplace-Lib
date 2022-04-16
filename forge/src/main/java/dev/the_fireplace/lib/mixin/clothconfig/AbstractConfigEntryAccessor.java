package dev.the_fireplace.lib.mixin.clothconfig;

import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(value = AbstractConfigEntry.class, remap = false)
public interface AbstractConfigEntryAccessor<T>
{
    @Accessor
    Supplier<Optional<Component>> getErrorSupplier();
}
