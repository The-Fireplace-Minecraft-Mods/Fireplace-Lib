package dev.the_fireplace.lib.teleport;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.teleport.injectables.SafePosition;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;

import java.util.Optional;

@Implementation
public final class SafePositionImpl implements SafePosition
{
    @Override
    public Optional<Vec3d> findBy(EntityType<?> entityType, CollisionView world, BlockPos blockPos) {
        return RespawnAnchorBlock.findRespawnPosition(entityType, world, blockPos);
    }

    @Override
    public boolean canSpawnInside(EntityType<?> entityType, BlockState state) {
        return !entityType.isInvalidSpawn(state);
    }
}
