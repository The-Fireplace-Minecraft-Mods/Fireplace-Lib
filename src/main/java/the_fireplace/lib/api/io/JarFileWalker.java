package the_fireplace.lib.api.io;

import the_fireplace.lib.impl.io.JarFileWalkerImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;

public interface JarFileWalker {
    static JarFileWalker getInstance() {
        //noinspection deprecation
        return JarFileWalkerImpl.INSTANCE;
    }
    Collection<Path> getFiles(String path) throws IOException, URISyntaxException;
}
