package dev.the_fireplace.lib.api.inventory.injectables;

import net.minecraft.inventory.Inventory;

public interface InventoryArrangement
{
    void swapSlotContents(Inventory inventory, int slot1, int slot2);
}
