package dev.the_fireplace.lib.domain.io;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import net.minecraft.resources.ResourceLocation;

public interface HierarchicalConfigWriter
{
    boolean write(HierarchicalConfig writable, String domain, String id);

    boolean write(HierarchicalConfig writable, String domain, ResourceLocation id);

    boolean delete(String domain, String id);

    boolean delete(String domain, ResourceLocation id);
}
