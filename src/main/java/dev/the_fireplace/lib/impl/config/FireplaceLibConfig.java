package dev.the_fireplace.lib.impl.config;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.storage.ThreadsafeLazySavable;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageType;
import dev.the_fireplace.lib.impl.FireplaceLib;

public final class FireplaceLibConfig extends ThreadsafeLazySavable {
    private static final FireplaceLibConfig INSTANCE = new FireplaceLibConfig();

    public static FireplaceLibConfig getInstance() {
        return INSTANCE;
    }

    private FireplaceLibConfig() {
        super(0);
    }

    private String locale = "en_us";

    private short essentialThreadPoolSize = 256;
    private short nonEssentialThreadPoolSize = 128;

    @Override
    public void readFrom(StorageReadBuffer reader) {
        locale = reader.readString("locale", locale);
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

    public String getLocale() {
        return locale;
    }

    public short getEssentialThreadPoolSize() {
        return essentialThreadPoolSize;
    }

    public short getNonEssentialThreadPoolSize() {
        return nonEssentialThreadPoolSize;
    }

    @Override
    public String getDatabase() {
        return "";
    }

    @Override
    public String getTable() {
        return "";
    }

    @Override
    public String getId() {
        return FireplaceLib.MODID;
    }

    @Override
    public StorageType getType() {
        return StorageType.CONFIG;
    }
}
