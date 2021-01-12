package the_fireplace.lib.api.io;

import the_fireplace.lib.impl.io.FileNames;

import java.util.UUID;

public interface FileNameResolver {
    static FileNameResolver getInstance() {
        //noinspection deprecation
        return FileNames.INSTANCE;
    }

    String jsonFileNameFromUUID(UUID uuid);
}
