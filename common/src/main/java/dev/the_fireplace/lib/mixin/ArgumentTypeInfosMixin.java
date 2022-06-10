package dev.the_fireplace.lib.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.the_fireplace.lib.command.helpers.ArgumentTypeFactoryImpl;
import dev.the_fireplace.lib.command.helpers.OfflinePlayerArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"unused", "SameParameterValue", "ResultOfMethodCallIgnored", "UnusedReturnValue"})
@Mixin(ArgumentTypeInfos.class)
public abstract class ArgumentTypeInfosMixin
{
    @Invoker
    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> callRegister(
        Registry<ArgumentTypeInfo<?, ?>> registry,
        String id,
        Class<? extends A> clazz,
        ArgumentTypeInfo<A, T> argumentTypeInfo
    ) {
        return null;
    }

    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void bootstrap(Registry<ArgumentTypeInfo<?, ?>> registry, CallbackInfoReturnable<ArgumentTypeInfo<?, ?>> cir) {
        callRegister(registry, "offline_player", OfflinePlayerArgumentType.class, ArgumentTypeFactoryImpl.OFFLINE_PLAYER_ARGUMENT_SERIALIZER);
    }
}
