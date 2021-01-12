package the_fireplace.lib.api.io;

import com.google.gson.JsonObject;
import the_fireplace.lib.impl.io.JsonObjectReaderFactoryImpl;

import java.io.File;

public interface JsonObjectReaderFactory {
    static JsonObjectReaderFactory getInstance() {
        //noinspection deprecation
        return JsonObjectReaderFactoryImpl.INSTANCE;
    }

    JsonObjectReader create(JsonObject obj);
    JsonObjectReader create(File file);
}
