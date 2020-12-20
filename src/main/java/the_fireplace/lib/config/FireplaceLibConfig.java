package the_fireplace.lib.config;

import com.google.gson.JsonObject;
import the_fireplace.lib.FireplaceLib;
import the_fireplace.lib.api.io.JsonReadable;
import the_fireplace.lib.api.io.JsonReader;
import the_fireplace.lib.api.io.JsonWritable;

import java.io.File;

import static the_fireplace.lib.api.io.Directories.CONFIG_DIRECTORY;

public class FireplaceLibConfig implements JsonReadable, JsonWritable {
    private static final File CONFIG_FILE = new File(CONFIG_DIRECTORY, FireplaceLib.MODID + ".json5");

    private static FireplaceLibConfig instance = null;

    public static FireplaceLibConfig getInstance() {
        if (instance == null) {
            instance = new FireplaceLibConfig();
        }
        return instance;
    }

    private FireplaceLibConfig() {
        readFromJson(JsonReader.create(CONFIG_FILE));
        save();
    }

    private String locale;

    private short essentialThreadPoolSize = 256;
    private short nonEssentialThreadPoolSize = 128;

    @Override
    public void readFromJson(JsonReader reader) {
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
        writeToJson(CONFIG_FILE);
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
