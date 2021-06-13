package dev.the_fireplace.lib.api.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;

public interface JarFileWalker {
    Collection<Path> getFiles(String path) throws IOException, URISyntaxException;
}
