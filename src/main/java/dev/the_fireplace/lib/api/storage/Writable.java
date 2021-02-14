package dev.the_fireplace.lib.api.storage;

import com.google.gson.JsonObject;

public interface Writable extends Storable {
    JsonObject toJson();
}
