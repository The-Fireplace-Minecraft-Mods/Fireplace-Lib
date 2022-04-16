package dev.the_fireplace.lib.api.uuid.injectables;

import java.util.UUID;

public interface EmptyUUID
{
    UUID get();

    boolean is(UUID uuid);
}
