package dev.the_fireplace.lib.api.teleport.injectables;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public interface SafePosition
{
    Optional<Vec3> findBy(EntityType<?> entityType, CollisionGetter world, BlockPos blockPos);

    boolean canSpawnInside(EntityType<?> entityType, BlockState state);
}
