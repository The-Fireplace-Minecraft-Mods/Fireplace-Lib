package the_fireplace.lib.api.io;

import java.io.File;

public interface JsonReadable {
    void readFromJson(JsonObjectReader reader);

    default void load(File file) {
        JsonObjectReader reader = JsonObjectReaderFactory.getInstance().create(file);
        readFromJson(reader);
    }
}
