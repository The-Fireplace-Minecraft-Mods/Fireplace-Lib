package dev.the_fireplace.lib.api.teleport.injectables;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public interface Teleporter
{
    Entity teleport(Entity entity, ServerLevel targetWorld, BlockPos targetPos);

    Entity teleport(Entity entity, ServerLevel targetWorld, double targetX, double targetY, double targetZ);
}
