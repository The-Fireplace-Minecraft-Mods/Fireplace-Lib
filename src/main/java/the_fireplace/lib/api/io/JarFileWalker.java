package the_fireplace.lib.api.io;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class JarFileWalker {
    private static final ConcurrentMap<String, Object> LOCKS = new ConcurrentHashMap<>();

    public static Collection<Path> getFiles(String path) throws IOException, URISyntaxException {
        URI uri = JarFileWalker.class.getResource(path).toURI();
        Stream<Path> paths;
        if ("jar".equals(uri.getScheme())) {
            paths = safeWalkJar(path, uri);
        } else {
            paths = Files.walk(Paths.get(path));
        }
        return paths.collect(Collectors.toSet());
    }

    private static Stream<Path> safeWalkJar(String path, URI uri) throws IOException {
        synchronized (getLock(uri)) {
            try (FileSystem fs = getFileSystem(uri)) {
                return Files.walk(fs.getPath(path));
            }
        }
    }

    private static Object getLock(URI uri) {
        String fileName = parseFileName(uri);
        LOCKS.computeIfAbsent(fileName, s -> new Object());
        return LOCKS.get(fileName);
    }

    private static String parseFileName(URI uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        return schemeSpecificPart.substring(0, schemeSpecificPart.indexOf("!"));
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }
}
