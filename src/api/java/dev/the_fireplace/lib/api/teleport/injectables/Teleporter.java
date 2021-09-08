package dev.the_fireplace.lib.api.teleport.injectables;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface Teleporter {
    Entity teleport(Entity entity, ServerWorld targetWorld, BlockPos targetPos);

    Entity teleport(Entity entity, ServerWorld targetWorld, double targetX, double targetY, double targetZ);
}
