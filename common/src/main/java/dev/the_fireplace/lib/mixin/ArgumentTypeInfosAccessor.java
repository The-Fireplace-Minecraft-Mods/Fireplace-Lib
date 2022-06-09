package dev.the_fireplace.lib.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArgumentTypeInfos.class)
public interface ArgumentTypeInfosAccessor
{
    @Invoker
    static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> callRegister(
        Registry<ArgumentTypeInfo<?, ?>> registry,
        String id,
        Class<? extends A> clazz,
        ArgumentTypeInfo<A, T> argumentTypeInfo
    ) {
        return null;
    }
}
