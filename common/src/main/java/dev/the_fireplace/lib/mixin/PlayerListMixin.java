package dev.the_fireplace.lib.mixin;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.network.NetworkEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin
{
    @Inject(at = @At("HEAD"), method = "remove")
    public void remove(ServerPlayer player, CallbackInfo info) {
        FireplaceLibConstants.getInjector().getInstance(NetworkEvents.class).onDisconnected(player.getUUID());
    }
}
