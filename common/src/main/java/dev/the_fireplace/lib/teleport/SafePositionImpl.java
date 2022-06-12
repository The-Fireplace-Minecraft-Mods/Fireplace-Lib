package dev.the_fireplace.lib.teleport;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.teleport.injectables.SafePosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Implementation
public final class SafePositionImpl implements SafePosition
{
    private final ImmutableList<Vec3i> xzOffsets = ImmutableList.of(
        new Vec3i(0, 0, -1),
        new Vec3i(-1, 0, 0),
        new Vec3i(0, 0, 1),
        new Vec3i(1, 0, 0),
        new Vec3i(-1, 0, -1),
        new Vec3i(1, 0, -1),
        new Vec3i(-1, 0, 1),
        new Vec3i(1, 0, 1)
    );
    private final ImmutableList<Vec3i> teleportAreaOffsets = new ImmutableList.Builder<Vec3i>()
        .addAll(xzOffsets)
        .addAll(xzOffsets.stream().map(Vec3i::below).iterator())
        .addAll(xzOffsets.stream().map(v3i -> v3i.below(-1)).iterator())
        .add(new Vec3i(0, 1, 0))
        .build();
    private final Set<Block> blocksNothingCanSpawnInside = Sets.newHashSet(
        Blocks.WITHER_ROSE,
        Blocks.SWEET_BERRY_BUSH,
        Blocks.CACTUS
    );

    @Override
    public Optional<Vec3> findBy(EntityType<?> entityType, CollisionGetter world, BlockPos blockPos) {
        return findRespawnPosition(entityType, world, blockPos);
    }

    @Override
    public boolean canSpawnInside(EntityType<?> entityType, BlockState state) {
        boolean blockDealsFireDamage = state.getBlock().equals(Blocks.FIRE) || state.getBlock().equals(Blocks.MAGMA_BLOCK) || isLitCampfire(state) || state.getBlock().equals(Blocks.LAVA);
        if (!entityType.fireImmune() && blockDealsFireDamage) {
            return false;
        }

        return !blocksNothingCanSpawnInside.contains(state.getBlock());
    }

    private boolean isLitCampfire(BlockState state) {
        return state.getBlock() == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT);
    }

    private Optional<Vec3> findRespawnPosition(EntityType<?> entity, CollisionGetter world, BlockPos pos) {
        Optional<Vec3> optional = findSafeNearbyPosition(entity, world, pos, true);
        return optional.isPresent() ? optional : findSafeNearbyPosition(entity, world, pos, false);
    }

    private Optional<Vec3> findSafeNearbyPosition(EntityType<?> entityType, CollisionGetter world, BlockPos blockPos, boolean protectAgainstHazardousSpawn) {
        BlockPos.MutableBlockPos mutableTargetPos = new BlockPos.MutableBlockPos();
        UnmodifiableIterator<Vec3i> offsetIterator = teleportAreaOffsets.iterator();

        Vec3 safePosition;
        do {
            if (!offsetIterator.hasNext()) {
                return Optional.empty();
            }

            Vec3i checkOffset = offsetIterator.next();
            mutableTargetPos.set(blockPos).move(checkOffset.getX(), checkOffset.getY(), checkOffset.getZ());
            safePosition = checkPositionForSafety(entityType, world, mutableTargetPos, protectAgainstHazardousSpawn);
        } while (safePosition == null);

        return Optional.of(safePosition);
    }

    @Nullable
    private Vec3 checkPositionForSafety(EntityType<?> entityType, CollisionGetter world, BlockPos blockPos, boolean protectAgainstHazardousSpawn) {
        if (protectAgainstHazardousSpawn && !canSpawnInside(entityType, world.getBlockState(blockPos))) {
            return null;
        }
        double dismountHeight = getDismountHeight(getCollisionShape(world, blockPos), () -> getCollisionShape(world, blockPos.below()));
        if (!canDismountInBlock(dismountHeight)) {
            return null;
        } else if (protectAgainstHazardousSpawn && dismountHeight <= 0.0D && !canSpawnInside(entityType, world.getBlockState(blockPos.below()))) {
            return null;
        } else {
            Vec3 vec3d = getCenteredSpawnVector(blockPos, dismountHeight);
            return world.getBlockCollisions(null, getCollisionBoxForPosition(entityType.getDimensions(), vec3d.x(), vec3d.y(), vec3d.z()))
                .allMatch(VoxelShape::isEmpty)
                ? vec3d
                : null;
        }
    }

    private double getDismountHeight(VoxelShape blockCollisionShape, Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
        if (!blockCollisionShape.isEmpty()) {
            return blockCollisionShape.max(Direction.Axis.Y);
        } else {
            double dismountHeight = belowBlockCollisionShapeGetter.get().max(Direction.Axis.Y);
            return dismountHeight >= 1.0D ? dismountHeight - 1.0D : Double.NEGATIVE_INFINITY;
        }
    }

    private VoxelShape getCollisionShape(BlockGetter world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return !(blockState.getBlock() instanceof LadderBlock || blockState.getBlock() instanceof VineBlock)
            && (!(blockState.getBlock() instanceof TrapDoorBlock) || !(Boolean) blockState.getValue(TrapDoorBlock.OPEN))
            ? blockState.getCollisionShape(world, pos)
            : Shapes.empty();
    }

    private boolean canDismountInBlock(double height) {
        return !Double.isInfinite(height) && height < 1.0D;
    }

    private Vec3 getCenteredSpawnVector(Vec3i vec, double deltaY) {
        return new Vec3((double) vec.getX() + 0.5D, (double) vec.getY() + deltaY, (double) vec.getZ() + 0.5D);
    }

    public AABB getCollisionBoxForPosition(EntityDimensions entityDimensions, double x, double y, double z) {
        float widthOffset = entityDimensions.width / 2.0F;
        float heightOffset = entityDimensions.height;
        return new AABB(x - (double) widthOffset, y, z - (double) widthOffset, x + (double) widthOffset, y + (double) heightOffset, z + (double) widthOffset);
    }
}
