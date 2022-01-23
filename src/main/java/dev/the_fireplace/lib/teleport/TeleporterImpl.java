package dev.the_fireplace.lib.teleport;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.teleport.injectables.Teleporter;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;

@Implementation
public final class TeleporterImpl implements Teleporter
{
    @Override
    public Entity teleport(Entity entity, ServerWorld targetWorld, BlockPos targetPos) {
        return teleport(entity, targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }

    @Override
    public Entity teleport(Entity entity, ServerWorld targetWorld, double targetX, double targetY, double targetZ) {
        if (entity instanceof ServerPlayerEntity) {
            preloadTargetChunk(targetWorld, targetX, targetZ);
            ((ServerPlayerEntity) entity).teleport(targetWorld, targetX, targetY, targetZ, entity.getYaw(), entity.getPitch());
            return entity;
        }
        DimensionType targetDimensionType = targetWorld.getDimension();
        Entity entityInTargetWorld = targetDimensionType.equals(entity.world.getDimension()) ? entity : entity.moveToWorld(targetWorld);
        if (entityInTargetWorld != null) {
            entityInTargetWorld.teleport(targetX, targetY, targetZ);
            return entityInTargetWorld;
        } else {
            FireplaceLib.getLogger().warn("Entity was removed before it could be moved to target world.", new Exception("Stack Trace"));
            return entity;
        }
    }

    private void preloadTargetChunk(ServerWorld targetWorld, double targetBlockX, double targetBlockZ) {
        int chunkX = ChunkSectionPos.getSectionCoord(targetBlockX);
        int chunkZ = ChunkSectionPos.getSectionCoord(targetBlockZ);
        targetWorld.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);
    }
}
