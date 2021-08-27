package dev.the_fireplace.lib.domain.io;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;

public interface HierarchicalConfigReader {
    void readTo(HierarchicalConfig readable, String domain, String id);
}
