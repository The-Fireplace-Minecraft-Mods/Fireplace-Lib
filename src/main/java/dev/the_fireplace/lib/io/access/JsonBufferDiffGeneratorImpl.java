package dev.the_fireplace.lib.io.access;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;
import dev.the_fireplace.lib.domain.io.JsonBufferDiffGenerator;

import java.lang.reflect.Type;
import java.util.Map;

@Implementation
public final class JsonBufferDiffGeneratorImpl implements JsonBufferDiffGenerator {
    @Override
    public SimpleBuffer generateLeftDiff(JsonStorageWriteBuffer buffer1, JsonStorageWriteBuffer buffer2) {
        if (buffer1.getObj() == buffer2.getObj()) {
            return new SimpleJsonBuffer(new JsonObject());
        }
        Gson g = new Gson();
        Type mapType = new TypeToken<Map<String, JsonElement>>(){}.getType();
        Map<String, JsonElement> firstMap = g.fromJson(buffer1.getObj(), mapType);
        Map<String, JsonElement> secondMap = g.fromJson(buffer2.getObj(), mapType);
        MapDifference<String, JsonElement> diff = Maps.difference(firstMap, secondMap);

        JsonObject diffOutput = new JsonObject();

        for (Map.Entry<String, MapDifference.ValueDifference<JsonElement>> entry : diff.entriesDiffering().entrySet()) {
            diffOutput.add(entry.getKey(), entry.getValue().leftValue());
        }

        for (Map.Entry<String, JsonElement> entry : diff.entriesOnlyOnLeft().entrySet()) {
            diffOutput.add(entry.getKey(), entry.getValue());
        }

        for (String entryKey : diff.entriesOnlyOnRight().keySet()) {
            diffOutput.add(entryKey, null);
        }


        return new SimpleJsonBuffer(diffOutput);
    }
}
