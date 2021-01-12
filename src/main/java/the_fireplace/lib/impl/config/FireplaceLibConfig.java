package the_fireplace.lib.impl.config;

import com.google.gson.JsonObject;
import the_fireplace.lib.api.io.*;
import the_fireplace.lib.impl.FireplaceLib;

import java.io.File;

public class FireplaceLibConfig implements JsonReadable, JsonWritable {
    private static File getConfigFile() {
        return DirectoryResolver.getInstance().getConfigPath().resolve(FireplaceLib.MODID + ".json5").toFile();
    }

    private static FireplaceLibConfig instance = null;

    public static FireplaceLibConfig getInstance() {
        if (instance == null) {
            instance = new FireplaceLibConfig();
        }
        return instance;
    }

    private FireplaceLibConfig() {
        readFromJson(JsonObjectReaderFactory.getInstance().create(getConfigFile()));
        save();
    }

    private String locale;

    private short essentialThreadPoolSize = 256;
    private short nonEssentialThreadPoolSize = 128;

    @Override
    public void readFromJson(JsonObjectReader reader) {
        locale = reader.readString("locale", "en_us");
        essentialThreadPoolSize = reader.readShort("essentialThreadPoolSize", essentialThreadPoolSize);
        nonEssentialThreadPoolSize = reader.readShort("nonEssentialThreadPoolSize", nonEssentialThreadPoolSize);
    }

    @Override
    public JsonObject toJson() {
        JsonObject instanceJson = new JsonObject();

        instanceJson.addProperty("locale", locale);
        instanceJson.addProperty("essentialThreadPoolSize", essentialThreadPoolSize);
        instanceJson.addProperty("nonEssentialThreadPoolSize", nonEssentialThreadPoolSize);

        return instanceJson;
    }

    private void save() {
        writeToJson(getConfigFile());
    }

    public String getLocale() {
        return locale;
    }

    public short getEssentialThreadPoolSize() {
        return essentialThreadPoolSize;
    }

    public short getNonEssentialThreadPoolSize() {
        return nonEssentialThreadPoolSize;
    }
}
