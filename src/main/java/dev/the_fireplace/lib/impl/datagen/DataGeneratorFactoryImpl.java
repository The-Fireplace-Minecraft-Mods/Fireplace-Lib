package dev.the_fireplace.lib.impl.datagen;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.datagen.injectables.DataGeneratorFactory;
import net.minecraft.data.DataGenerator;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

@Implementation
@Singleton
public final class DataGeneratorFactoryImpl implements DataGeneratorFactory {

    @Override
    public DataGenerator createAdditive(Path outputDirectory) {
        return createAdditive(outputDirectory, Collections.emptySet());
    }

    @Override
    public DataGenerator createAdditive(Path outputDirectory, Collection<Path> nbtProviderInputs) {
        return new AdditiveDataGenerator(outputDirectory, nbtProviderInputs);
    }

    @Override
    public DataGenerator createDestructive(Path outputDirectory) {
        return createDestructive(outputDirectory, Collections.emptySet());
    }

    @Override
    public DataGenerator createDestructive(Path outputDirectory, Collection<Path> nbtProviderInputs) {
        return new DataGenerator(outputDirectory, nbtProviderInputs);
    }
}
