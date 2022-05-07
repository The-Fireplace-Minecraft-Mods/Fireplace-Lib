package dev.the_fireplace.lib.mixin;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.network.ClientNetworkEvents;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public final class ClientPacketListenerMixin
{
    @Inject(at = @At("RETURN"), method = "handleLogin")
    public void onGameJoin(ClientboundLoginPacket packet, CallbackInfo info) {
        DIContainer.get().getInstance(ClientNetworkEvents.class).onConnectToServer();
    }
}
