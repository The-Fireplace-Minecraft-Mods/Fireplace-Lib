package dev.the_fireplace.lib.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.the_fireplace.lib.command.helpers.ArgumentTypeFactoryImpl;
import dev.the_fireplace.lib.command.helpers.OfflinePlayerArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArgumentTypes.class)
public final class ArgumentTypesMixin
{
    private static final ResourceLocation ENTITY_ARGUMENT_ID = new ResourceLocation("minecraft", "entity");

    @Inject(at = @At("HEAD"), method = "serialize", cancellable = true)
    private static <T extends ArgumentType<?>> void hijackOfflinePlayerPacketSerializationForVanillaClientCompatibility(
        FriendlyByteBuf packetByteBuf,
        T argumentType,
        CallbackInfo ci
    ) {
        if (argumentType instanceof OfflinePlayerArgumentType) {
            packetByteBuf.writeResourceLocation(ENTITY_ARGUMENT_ID);
            ArgumentTypeFactoryImpl.OFFLINE_PLAYER_ARGUMENT_SERIALIZER.serializeToNetwork((OfflinePlayerArgumentType) argumentType, packetByteBuf);
            ci.cancel();
        }
    }
}
