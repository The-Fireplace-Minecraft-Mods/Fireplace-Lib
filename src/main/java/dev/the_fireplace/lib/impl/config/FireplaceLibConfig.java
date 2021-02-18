package dev.the_fireplace.lib.impl.config;

import dev.the_fireplace.lib.api.storage.LazyConfig;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;
import dev.the_fireplace.lib.impl.FireplaceLib;

public final class FireplaceLibConfig extends LazyConfig {
    private static final FireplaceLibConfig INSTANCE = new FireplaceLibConfig();
    private final Access access = new Access();

    public static Access getInstance() {
        return INSTANCE.access;
    }

    private FireplaceLibConfig() {}

    private String locale = "en_us";

    private short essentialThreadPoolSize = 256;
    private short nonEssentialThreadPoolSize = 128;

    @Override
    public void readFrom(StorageReadBuffer buffer) {
        locale = buffer.readString("locale", locale);
        essentialThreadPoolSize = buffer.readShort("essentialThreadPoolSize", essentialThreadPoolSize);
        nonEssentialThreadPoolSize = buffer.readShort("nonEssentialThreadPoolSize", nonEssentialThreadPoolSize);
    }

    @Override
    public void writeTo(StorageWriteBuffer buffer) {
        buffer.writeString("locale", locale);
        buffer.writeShort("essentialThreadPoolSize", essentialThreadPoolSize);
        buffer.writeShort("nonEssentialThreadPoolSize", nonEssentialThreadPoolSize);
    }

    @Override
    public String getId() {
        return FireplaceLib.MODID;
    }

    public final class Access {
        private Access(){}
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
}
