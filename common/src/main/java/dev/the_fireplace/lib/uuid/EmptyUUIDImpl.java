package dev.the_fireplace.lib.uuid;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;

import javax.inject.Singleton;
import java.util.UUID;

@Implementation
@Singleton
public final class EmptyUUIDImpl implements EmptyUUID
{
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    @Override
    public UUID get() {
        return EMPTY_UUID;
    }

    @Override
    public boolean is(UUID uuid) {
        return EMPTY_UUID.equals(uuid);
    }
}
