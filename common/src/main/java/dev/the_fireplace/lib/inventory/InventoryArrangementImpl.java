package dev.the_fireplace.lib.inventory;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.inventory.injectables.InventoryArrangement;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

@Implementation
public final class InventoryArrangementImpl implements InventoryArrangement
{
    @Override
    public void swapSlotContents(Container inventory, int slot1, int slot2) {
        ItemStack stack1 = inventory.removeItemNoUpdate(slot1);
        ItemStack stack2 = inventory.removeItemNoUpdate(slot2);
        inventory.setItem(slot1, stack2);
        inventory.setItem(slot2, stack1);
    }
}
