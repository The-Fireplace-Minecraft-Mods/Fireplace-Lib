package dev.the_fireplace.lib.domain.io;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;

public interface HierarchicalConfigWriter {
    boolean write(HierarchicalConfig writable, String domain, String id);
    boolean delete(HierarchicalConfig writable, String domain, String id);
}
