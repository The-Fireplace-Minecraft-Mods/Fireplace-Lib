package dev.the_fireplace.lib.impl.io;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.io.JarFileWalker;

import javax.inject.Singleton;
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

@Implementation
@Singleton
public final class JarFileWalkerImpl implements JarFileWalker {
    private final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<>();

    @Override
    public Collection<Path> getFiles(String path) throws IOException, URISyntaxException {
        URI uri = JarFileWalkerImpl.class.getResource(path).toURI();
        Stream<Path> paths;
        if ("jar".equals(uri.getScheme())) {
            paths = safeWalkJar(path, uri);
        } else {
            paths = Files.walk(Paths.get(path));
        }
        return paths.collect(Collectors.toSet());
    }

    private Stream<Path> safeWalkJar(String path, URI uri) throws IOException {
        synchronized (getLock(uri)) {
            try (FileSystem fs = getFileSystem(uri)) {
                return Files.walk(fs.getPath(path));
            }
        }
    }

    private Object getLock(URI uri) {
        String fileName = parseFileName(uri);
        locks.computeIfAbsent(fileName, s -> new Object());
        return locks.get(fileName);
    }

    private String parseFileName(URI uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        return schemeSpecificPart.substring(0, schemeSpecificPart.indexOf('!'));
    }

    private FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }
}
