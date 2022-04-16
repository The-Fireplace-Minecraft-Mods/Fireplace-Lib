package dev.the_fireplace.lib.api.inventory.injectables;

import net.minecraft.world.Container;

public interface InventoryArrangement
{
    void swapSlotContents(Container inventory, int slot1, int slot2);
}
