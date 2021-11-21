package dev.the_fireplace.lib.inventory;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.inventory.injectables.InventorySearcher;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@Implementation
public final class InventorySearcherImpl implements InventorySearcher
{
    @Override
    public boolean hasSlotMatching(Inventory inventory, Predicate<ItemStack> matcher) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            if (matcher.test(inventory.getStack(slot))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Optional<Integer> findFirstMatchingSlot(Inventory inventory, Predicate<ItemStack> matcher) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            if (matcher.test(inventory.getStack(slot))) {
                return Optional.of(slot);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Integer> findMatchingSlots(Inventory inventory, Predicate<ItemStack> matcher) {
        IntList slotList = new IntArrayList();

        for (int slot = 0; slot < inventory.size(); slot++) {
            if (matcher.test(inventory.getStack(slot))) {
                slotList.add(slot);
            }
        }

        return slotList;
    }

    @Override
    public Multimap<Integer, Integer> getMatchingSlotsByPriority(Inventory inventory, Predicate<ItemStack> matcher, ToIntFunction<ItemStack> priorityMapper) {
        IntList slotList = new IntArrayList();

        for (int slot = 0; slot < inventory.size(); slot++) {
            if (matcher.test(inventory.getStack(slot))) {
                slotList.add(slot);
            }
        }

        Multimap<Integer, Integer> slotPriorityMap = LinkedHashMultimap.create();

        for (int slot : slotList) {
            slotPriorityMap.put(priorityMapper.applyAsInt(inventory.getStack(slot)), slot);
        }

        slotPriorityMap = sortByKey(slotPriorityMap);

        return slotPriorityMap;
    }

    @Override
    public Multimap<Integer, Integer> getSlotsByPriority(Inventory inventory, ToIntFunction<ItemStack> priorityMapper) {
        Multimap<Integer, Integer> slotPriorityMap = LinkedHashMultimap.create();

        for (int slot = 0; slot < inventory.size(); slot++) {
            slotPriorityMap.put(priorityMapper.applyAsInt(inventory.getStack(slot)), slot);
        }

        slotPriorityMap = sortByKey(slotPriorityMap);

        return slotPriorityMap;
    }

    private Multimap<Integer, Integer> sortByKey(Multimap<Integer, Integer> slotPriorityMap) {
        slotPriorityMap = slotPriorityMap.entries().stream()
            .sorted((i1, i2) -> Integer.compare(i2.getKey(), i1.getKey()))
            .collect(Multimaps.toMultimap(Map.Entry::getKey, Map.Entry::getValue, LinkedHashMultimap::create));
        return slotPriorityMap;
    }
}
