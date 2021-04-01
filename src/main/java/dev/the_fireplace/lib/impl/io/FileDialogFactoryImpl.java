package dev.the_fireplace.lib.impl.io;

import dev.the_fireplace.lib.api.client.io.FileDialogFactory;
import dev.the_fireplace.lib.api.io.FilePathStorage;
import dev.the_fireplace.lib.impl.FireplaceLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        applyFileFilter(allowedFileTypePatterns, allowedFileTypesDescription, fileChooser);
        fileChooser.setCurrentDirectory(new File(rememberedPath != null ? rememberedPath : System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File selectedFile = fileChooser.getSelectedFile();
        storePathToMemory(rememberPath, displayedTitle, selectedFile.getPath());

        return selectedFile;
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        applyFileFilter(allowedFileTypePatterns, allowedFileTypesDescription, fileChooser);
        fileChooser.setCurrentDirectory(new File(rememberedPath != null ? rememberedPath : System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File[] selectedFiles = fileChooser.getSelectedFiles();
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        applyFileFilter(allowedFileTypePatterns, allowedFileTypesDescription, fileChooser);
        fileChooser.setCurrentDirectory(new File(rememberedPath != null ? rememberedPath : System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File selectedFile = fileChooser.getSelectedFile();
        storePathToMemory(rememberPath, displayedTitle, selectedFile.getPath());

        return selectedFile;
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

    private void applyFileFilter(@Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription, JFileChooser fileChooser) {
        if (allowedFileTypePatterns != null) {
            if (allowedFileTypesDescription == null) {
                allowedFileTypesDescription = String.join("|", allowedFileTypePatterns);
            }
            ArrayList<String> extensionNames = new ArrayList<>(allowedFileTypePatterns.length);
            boolean canFilterFiles = true;
            for (String pattern: allowedFileTypePatterns) {
                if (ALPHANUMERIC_PATTERN.matcher(pattern).matches()) {
                    extensionNames.add(pattern);
                } else if (EXTENSION_PATTERN.matcher(pattern).matches()) {
                    extensionNames.add(pattern.substring(pattern.lastIndexOf('.') + 1));
                } else {
                    canFilterFiles = false;
                    break;
                }
            }
            if (canFilterFiles) {
                fileChooser.setFileFilter(new FileNameExtensionFilter(allowedFileTypesDescription, extensionNames.toArray(new String[0])));
            } else {
                FireplaceLib.getLogger().warn("Unable to filter files on 1.14.4- for pattern set {}.", String.join(" | ", allowedFileTypePatterns));
            }
        }
    }
}
