package dev.the_fireplace.lib.api.teleport.injectables;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;

import java.util.Optional;

public interface SafePosition
{
    Optional<Vec3d> findBy(EntityType<?> entityType, CollisionView world, BlockPos blockPos);

    boolean canSpawnInside(EntityType<?> entityType, BlockState state);
}
