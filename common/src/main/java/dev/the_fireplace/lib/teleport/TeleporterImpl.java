package dev.the_fireplace.lib.teleport;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.teleport.injectables.Teleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;

@Implementation
public final class TeleporterImpl implements Teleporter
{
    @Override
    public Entity teleport(Entity entity, ServerLevel targetWorld, BlockPos targetPos) {
        return teleport(entity, targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }

    @Override
    public Entity teleport(Entity entity, ServerLevel targetWorld, double targetX, double targetY, double targetZ) {
        if (entity instanceof ServerPlayer) {
            preloadTargetChunk(targetWorld, targetX, targetZ);
            ((ServerPlayer) entity).teleportTo(targetWorld, targetX, targetY, targetZ, entity.getYRot(), entity.getXRot());
            return entity;
        }
        DimensionType targetDimensionType = targetWorld.dimensionType();
        Entity entityInTargetWorld = targetDimensionType.equals(entity.level.dimensionType()) ? entity : entity.changeDimension(targetWorld);
        if (entityInTargetWorld != null) {
            entityInTargetWorld.teleportTo(targetX, targetY, targetZ);
            return entityInTargetWorld;
        } else {
            FireplaceLibConstants.getLogger().warn("Entity was removed before it could be moved to target world.", new Exception("Stack Trace"));
            return entity;
        }
    }

    private void preloadTargetChunk(ServerLevel targetWorld, double targetBlockX, double targetBlockZ) {
        int chunkX = SectionPos.posToSectionCoord(targetBlockX);
        int chunkZ = SectionPos.posToSectionCoord(targetBlockZ);
        targetWorld.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);
    }
}
