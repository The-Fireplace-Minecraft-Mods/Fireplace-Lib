package dev.the_fireplace.lib.api.inventory.injectables;

import com.google.common.collect.Multimap;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public interface InventorySearcher
{
    boolean hasSlotMatching(Inventory inventory, Predicate<ItemStack> matcher);

    Optional<Integer> findFirstMatchingSlot(Inventory inventory, Predicate<ItemStack> matcher);

    List<Integer> findMatchingSlots(Inventory inventory, Predicate<ItemStack> matcher);

    /**
     * Multimap of Priority -> Slot
     */
    Multimap<Integer, Integer> getMatchingSlotsByPriority(Inventory inventory, Predicate<ItemStack> matcher, ToIntFunction<ItemStack> priorityMapper);

    /**
     * Multimap of Priority -> Slot
     */
    Multimap<Integer, Integer> getSlotsByPriority(Inventory inventory, ToIntFunction<ItemStack> priorityMapper);
}
