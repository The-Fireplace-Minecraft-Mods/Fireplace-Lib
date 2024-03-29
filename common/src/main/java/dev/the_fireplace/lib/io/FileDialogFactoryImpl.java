package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.client.injectables.FileDialogFactory;
import dev.the_fireplace.lib.api.io.injectables.FilePathStorage;
import net.minecraft.network.chat.Component;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Implementation(environment = "CLIENT")
@Singleton
public final class FileDialogFactoryImpl implements FileDialogFactory
{

    private final FilePathStorage filePathStorage;

    @Inject
    public FileDialogFactoryImpl(FilePathStorage filePathStorage) {
        this.filePathStorage = filePathStorage;
    }

    @Override
    @Nullable
    public File showOpenFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showOpenFileDialog(Component.translatable(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File showOpenFileDialog(Component title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        PointerBuffer pointerBuffer = convertToPointerBuffer(allowedFileTypePatterns);
        String displayedTitle = title.getString();
        String rememberedPath = getPathFromMemory(rememberPath, displayedTitle);
        String filePath = TinyFileDialogs.tinyfd_openFileDialog(displayedTitle, rememberedPath, pointerBuffer, allowedFileTypesDescription, false);
        storePathToMemory(rememberPath, displayedTitle, filePath);

        return filePath != null ? new File(filePath) : null;
    }

    @Override
    @Nullable
    public File[] showOpenMultiFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showOpenMultiFileDialog(Component.translatable(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File[] showOpenMultiFileDialog(Component title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        PointerBuffer pointerBuffer = convertToPointerBuffer(allowedFileTypePatterns);
        String displayedTitle = title.getString();
        String rememberedPath = getPathFromMemory(rememberPath, displayedTitle);
        String filePath = TinyFileDialogs.tinyfd_openFileDialog(displayedTitle, rememberedPath, pointerBuffer, allowedFileTypesDescription, false);
        if (filePath == null) {
            return null;
        }
        storePathToMemory(rememberPath, displayedTitle, filePath);
        String[] filePaths = filePath.split("\\|");
        List<File> files = new ArrayList<>(filePaths.length);
        for (String path : filePaths) {
            files.add(new File(path));
        }

        return files.toArray(new File[0]);
    }

    @Override
    @Nullable
    public File showSaveFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showSaveFileDialog(Component.translatable(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File showSaveFileDialog(Component title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        PointerBuffer pointerBuffer = convertToPointerBuffer(allowedFileTypePatterns);
        String displayedTitle = title.getString();
        String rememberedPath = getPathFromMemory(rememberPath, displayedTitle);
        String filePath = TinyFileDialogs.tinyfd_saveFileDialog(displayedTitle, rememberedPath, pointerBuffer, allowedFileTypesDescription);
        storePathToMemory(rememberPath, displayedTitle, filePath);

        return filePath != null ? new File(filePath) : null;
    }

    @Nullable
    private String getPathFromMemory(boolean rememberPath, String pathKey) {
        String rememberedPath = rememberPath ? filePathStorage.getFilePath(pathKey) : null;
        if (rememberedPath != null && rememberedPath.isEmpty()) {
            rememberedPath = null;
        }
        return rememberedPath;
    }

    private void storePathToMemory(boolean rememberPath, String pathKey, @Nullable String filePath) {
        if (filePath != null && rememberPath) {
            filePathStorage.storeFilePath(pathKey, filePath);
        }
    }

    @Nullable
    private PointerBuffer convertToPointerBuffer(@Nullable String[] strings) {
        PointerBuffer fileTypePatternsPointerBuffer = null;
        if (strings != null && strings.length > 0) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                fileTypePatternsPointerBuffer = stack.mallocPointer(strings.length);
                for (String pattern : strings) {
                    fileTypePatternsPointerBuffer.put(stack.UTF8(pattern));
                }
                fileTypePatternsPointerBuffer.flip();
            }
        }

        return fileTypePatternsPointerBuffer;
    }
}
