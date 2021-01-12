package the_fireplace.lib.impl.io;

import the_fireplace.lib.api.io.FileNameResolver;

import java.util.UUID;

public final class FileNames implements FileNameResolver {
    @Deprecated
    public static final FileNameResolver INSTANCE = new FileNames();

    private FileNames(){}

    @Override
    public String jsonFileNameFromUUID(UUID uuid) {
        return uuid.toString()+".json";
    }
}
