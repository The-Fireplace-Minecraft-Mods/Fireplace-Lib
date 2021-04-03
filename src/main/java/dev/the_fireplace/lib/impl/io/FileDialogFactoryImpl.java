package dev.the_fireplace.lib.impl.io;

import dev.the_fireplace.lib.api.client.io.FileDialogFactory;
import dev.the_fireplace.lib.api.io.FilePathStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class FileDialogFactoryImpl implements FileDialogFactory {
    @Deprecated
    public static final FileDialogFactoryImpl INSTANCE = new FileDialogFactoryImpl();

    private final FilePathStorage filePathStorage;

    private FileDialogFactoryImpl() {
        this.filePathStorage = FilePathStorage.getInstance();
    }

    @Override
    @Nullable
    public File showOpenFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showOpenFileDialog(new TranslatableText(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File showOpenFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
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
        return showOpenMultiFileDialog(new TranslatableText(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File[] showOpenMultiFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
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
        for (String path: filePaths) {
            files.add(new File(path));
        }

        return files.toArray(new File[0]);
    }

    @Override
    @Nullable
    public File showSaveFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showSaveFileDialog(new TranslatableText(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File showSaveFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
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
