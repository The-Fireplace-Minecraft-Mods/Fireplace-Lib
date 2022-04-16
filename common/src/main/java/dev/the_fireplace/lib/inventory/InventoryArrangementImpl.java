package dev.the_fireplace.lib.inventory;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.inventory.injectables.InventoryArrangement;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Implementation
public final class InventoryArrangementImpl implements InventoryArrangement
{
    @Override
    public void swapSlotContents(Inventory inventory, int slot1, int slot2) {
        ItemStack stack1 = inventory.removeStack(slot1);
        ItemStack stack2 = inventory.removeStack(slot2);
        inventory.setStack(slot1, stack2);
        inventory.setStack(slot2, stack1);
    }
}
