package dev.the_fireplace.lib.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.the_fireplace.lib.command.helpers.ArgumentTypeFactoryImpl;
import dev.the_fireplace.lib.command.helpers.OfflinePlayerArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.protocol.game.ClientboundCommandsPacket.ArgumentNodeStub")
public final class ArgumentNodeStubMixin
{
    private static final ResourceLocation ENTITY_ARGUMENT_ID = new ResourceLocation("minecraft", "entity");

    @Inject(
        at = @At("HEAD"),
        method = "serializeCap(Lnet/minecraft/network/FriendlyByteBuf;Lnet/minecraft/commands/synchronization/ArgumentTypeInfo;Lnet/minecraft/commands/synchronization/ArgumentTypeInfo$Template;)V",
        cancellable = true
    )
    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void hijackOfflinePlayerPacketSerializationForVanillaClientCompatibility(
        FriendlyByteBuf packetByteBuf, ArgumentTypeInfo<A, T> argumentType, ArgumentTypeInfo.Template<A> argumentTypeTemplate, CallbackInfo ci
    ) {
        if (argumentType instanceof OfflinePlayerArgumentType.Serializer && argumentTypeTemplate instanceof OfflinePlayerArgumentType.Serializer.Template) {
            packetByteBuf.writeResourceLocation(ENTITY_ARGUMENT_ID);
            ArgumentTypeFactoryImpl.OFFLINE_PLAYER_ARGUMENT_SERIALIZER.serializeToNetwork((OfflinePlayerArgumentType.Serializer.Template) argumentTypeTemplate, packetByteBuf);
            ci.cancel();
        }
    }
}
