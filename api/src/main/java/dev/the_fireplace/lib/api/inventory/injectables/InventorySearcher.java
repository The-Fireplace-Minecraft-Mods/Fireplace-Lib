package dev.the_fireplace.lib.api.inventory.injectables;

import com.google.common.collect.Multimap;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public interface InventorySearcher
{
    boolean hasSlotMatching(Container inventory, Predicate<ItemStack> matcher);

    Optional<Integer> findFirstMatchingSlot(Container inventory, Predicate<ItemStack> matcher);

    List<Integer> findMatchingSlots(Container inventory, Predicate<ItemStack> matcher);

    /**
     * Multimap of Priority -> Slot
     */
    Multimap<Integer, Integer> getMatchingSlotsByPriority(Container inventory, Predicate<ItemStack> matcher, ToIntFunction<ItemStack> priorityMapper);

    /**
     * Multimap of Priority -> Slot
     */
    Multimap<Integer, Integer> getSlotsByPriority(Container inventory, ToIntFunction<ItemStack> priorityMapper);
}
