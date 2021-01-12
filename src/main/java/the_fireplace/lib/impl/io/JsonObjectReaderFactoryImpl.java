package the_fireplace.lib.impl.io;

import com.google.gson.JsonObject;
import the_fireplace.lib.api.io.JsonFileReader;
import the_fireplace.lib.api.io.JsonObjectReader;
import the_fireplace.lib.api.io.JsonObjectReaderFactory;

import java.io.File;

public class JsonObjectReaderFactoryImpl implements JsonObjectReaderFactory {
    @Deprecated
    public static final JsonObjectReaderFactory INSTANCE = new JsonObjectReaderFactoryImpl();

    private JsonObjectReaderFactoryImpl(){}

    @Override
    public JsonObjectReader create(JsonObject obj) {
        return new JsonObjectReaderImpl(obj);
    }

    @Override
    public JsonObjectReader create(File file) {
        JsonObject obj = JsonFileReader.getInstance().readJsonFile(file);
        if(obj == null)
            return create(new JsonObject());
        return create(obj);
    }
}
