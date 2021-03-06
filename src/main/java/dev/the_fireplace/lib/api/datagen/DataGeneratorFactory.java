package dev.the_fireplace.lib.api.datagen;

import dev.the_fireplace.lib.impl.datagen.DataGeneratorFactoryImpl;
import net.minecraft.data.DataGenerator;

import java.nio.file.Path;
import java.util.Collection;

public interface DataGeneratorFactory {
    static DataGeneratorFactory getInstance() {
        //noinspection deprecation
        return DataGeneratorFactoryImpl.INSTANCE;
    }

    /**
     * @see DataGeneratorFactory#createAdditive(Path, Collection)
     */
    DataGenerator createAdditive(Path outputDirectory);
    /**
     * Create a data generator that will only delete existing files from the target directory if they're being replaced
     */
    DataGenerator createAdditive(Path outputDirectory, Collection<Path> nbtProviderInputs);

    /**
     * @see DataGeneratorFactory#createDestructive(Path, Collection)
     */
    DataGenerator createDestructive(Path outputDirectory);
    /**
     * Create a data generator that will always delete previously existing files from the target directory before generating
     */
    DataGenerator createDestructive(Path outputDirectory, Collection<Path> nbtProviderInputs);
}
