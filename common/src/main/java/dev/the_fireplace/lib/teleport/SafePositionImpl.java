package dev.the_fireplace.lib.teleport;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.teleport.injectables.SafePosition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

@Implementation
public final class SafePositionImpl implements SafePosition
{
    @Override
    public Optional<Vec3> findBy(EntityType<?> entityType, CollisionGetter world, BlockPos blockPos) {
        return RespawnAnchorBlock.findStandUpPosition(entityType, world, blockPos);
    }

    @Override
    public boolean canSpawnInside(EntityType<?> entityType, BlockState state) {
        return !entityType.isBlockDangerous(state);
    }
}
