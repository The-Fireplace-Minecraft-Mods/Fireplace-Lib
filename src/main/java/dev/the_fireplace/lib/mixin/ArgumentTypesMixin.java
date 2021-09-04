package dev.the_fireplace.lib.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.the_fireplace.lib.command.helpers.ArgumentTypeFactoryImpl;
import dev.the_fireplace.lib.command.helpers.OfflinePlayerArgumentType;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArgumentTypes.class)
public final class ArgumentTypesMixin {
    private static final Identifier ENTITY_ARGUMENT_ID = new Identifier("minecraft", "entity");

    @Inject(at = @At("HEAD"), method = "toPacket", cancellable = true)
    private static <T extends ArgumentType<?>> void hijackOfflinePlayerPacketSerializationForVanillaClientCompatibility(
        PacketByteBuf packetByteBuf,
        T argumentType,
        CallbackInfo ci
    ) {
        if (argumentType instanceof OfflinePlayerArgumentType) {
            packetByteBuf.writeIdentifier(ENTITY_ARGUMENT_ID);
            ArgumentTypeFactoryImpl.OFFLINE_PLAYER_ARGUMENT_SERIALIZER.toPacket((OfflinePlayerArgumentType) argumentType, packetByteBuf);
            ci.cancel();
        }
    }
}
