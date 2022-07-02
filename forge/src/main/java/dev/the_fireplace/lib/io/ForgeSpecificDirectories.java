package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.io.LoaderSpecificDirectories;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Implementation
public final class ForgeSpecificDirectories implements LoaderSpecificDirectories
{
    @Override
    public Path getConfigPath() {
        return Path.of(FMLConfig.defaultConfigPath());
    }

    @Override
    public Optional<Path> getResource(String modId, String path) {
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(modId);
        if (modContainer.isEmpty()) {
            return Optional.empty();
        }
        Path resource = modContainer.get().getModInfo().getOwningFile().getFile().findResource(path);
        if (Files.exists(resource)) {
            return Optional.of(resource);
        }

        return Optional.empty();
    }
}
