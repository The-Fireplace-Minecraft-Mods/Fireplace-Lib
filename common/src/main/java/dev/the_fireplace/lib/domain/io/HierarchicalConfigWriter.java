package dev.the_fireplace.lib.domain.io;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import net.minecraft.util.Identifier;

public interface HierarchicalConfigWriter
{
    boolean write(HierarchicalConfig writable, String domain, String id);

    boolean write(HierarchicalConfig writable, String domain, Identifier id);

    boolean delete(String domain, String id);

    boolean delete(String domain, Identifier id);
}
