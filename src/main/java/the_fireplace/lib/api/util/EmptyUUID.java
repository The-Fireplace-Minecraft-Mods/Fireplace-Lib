package the_fireplace.lib.api.util;

import java.util.UUID;

public final class EmptyUUID {
    public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static boolean isEmpty(UUID uuid) {
        return EMPTY_UUID.equals(uuid);
    }
}
