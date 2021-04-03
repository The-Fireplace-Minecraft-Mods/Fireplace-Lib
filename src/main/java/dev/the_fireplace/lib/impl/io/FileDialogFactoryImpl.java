package dev.the_fireplace.lib.impl.io;

import dev.the_fireplace.lib.api.client.io.FileDialogFactory;
import dev.the_fireplace.lib.api.io.FilePathStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public final class FileDialogFactoryImpl implements FileDialogFactory {
    @Deprecated
    public static final FileDialogFactoryImpl INSTANCE = new FileDialogFactoryImpl();
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^\\*\\.[a-zA-Z0-9]+$");

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
        String displayedTitle = title.getString();
        String rememberedPath = getPathFromMemory(rememberPath, displayedTitle);
        FileDialog fileChooser = new FileDialog((Frame)null, displayedTitle);
        fileChooser.setMode(FileDialog.LOAD);
        fileChooser.setMultipleMode(false);
        applyFileFilter(allowedFileTypePatterns, fileChooser);
        fileChooser.setFile(rememberedPath != null ? rememberedPath : System.getProperty("user.home"));
        fileChooser.setVisible(true);
        String selectedFile = fileChooser.getFile();
        if (selectedFile == null) {
            return null;
        }
        storePathToMemory(rememberPath, displayedTitle, selectedFile);

        return new File(selectedFile);
    }

    @Override
    @Nullable
    public File[] showOpenMultiFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showOpenMultiFileDialog(new TranslatableText(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File[] showOpenMultiFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        String displayedTitle = title.getString();
        String rememberedPath = getPathFromMemory(rememberPath, displayedTitle);
        FileDialog fileChooser = new FileDialog((Frame)null, displayedTitle);
        fileChooser.setMode(FileDialog.LOAD);
        fileChooser.setMultipleMode(true);
        applyFileFilter(allowedFileTypePatterns, fileChooser);
        fileChooser.setFile(rememberedPath != null ? rememberedPath : System.getProperty("user.home"));
        fileChooser.setVisible(true);
        File[] selectedFiles = fileChooser.getFiles();
        if (selectedFiles == null) {
            return null;
        }
        storePathToMemory(rememberPath, displayedTitle, selectedFiles[0].getPath());

        return selectedFiles;
    }

    @Override
    @Nullable
    public File showSaveFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        return showSaveFileDialog(new TranslatableText(titleTranslationKey), rememberPath, allowedFileTypePatterns, allowedFileTypesDescription);
    }

    @Override
    @Nullable
    public File showSaveFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription) {
        String displayedTitle = title.getString();
        String rememberedPath = getPathFromMemory(rememberPath, displayedTitle);
        FileDialog fileChooser = new FileDialog((Frame)null, displayedTitle);
        fileChooser.setMode(FileDialog.SAVE);
        fileChooser.setMultipleMode(false);
        applyFileFilter(allowedFileTypePatterns, fileChooser);
        fileChooser.setFile(rememberedPath != null ? rememberedPath : System.getProperty("user.home"));
        fileChooser.setVisible(true);
        String selectedFile = fileChooser.getFile();
        if (selectedFile == null) {
            return null;
        }
        storePathToMemory(rememberPath, displayedTitle, selectedFile);

        return new File(selectedFile);
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

    private void applyFileFilter(@Nullable String[] allowedFileTypePatterns, FileDialog fileChooser) {
        if (allowedFileTypePatterns != null) {
            fileChooser.setFilenameFilter((file, s) -> {
                for (String pattern: allowedFileTypePatterns) {
                    if (s.matches(pattern)) {
                        return true;
                    }
                }
                return false;
            });
        }
    }
}
