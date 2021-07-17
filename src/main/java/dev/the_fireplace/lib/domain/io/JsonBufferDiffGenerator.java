package dev.the_fireplace.lib.domain.io;

import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;
import dev.the_fireplace.lib.io.access.JsonStorageWriteBuffer;

public interface JsonBufferDiffGenerator {
    /**
     * Generate a diff containing only the left-side values of the differences
     */
    SimpleBuffer generateLeftDiff(JsonStorageWriteBuffer buffer1, JsonStorageWriteBuffer buffer2);
}
