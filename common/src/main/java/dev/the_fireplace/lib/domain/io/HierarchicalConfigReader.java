package dev.the_fireplace.lib.domain.io;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import net.minecraft.resources.ResourceLocation;

public interface HierarchicalConfigReader
{
    void readTo(HierarchicalConfig readable, String domain, String id);

    void readTo(HierarchicalConfig readable, String domain, ResourceLocation id);
}
