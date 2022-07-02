package dev.the_fireplace.lib.domain.io;

import java.nio.file.Path;
import java.util.Optional;

public interface LoaderSpecificDirectories
{
    Path getConfigPath();

    Optional<Path> getResource(String modId, String path);
}
