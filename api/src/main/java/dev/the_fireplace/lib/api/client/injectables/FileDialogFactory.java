package dev.the_fireplace.lib.api.client.injectables;

import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Client side only
 */
public interface FileDialogFactory
{
    @Nullable
    File showOpenFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showOpenFileDialog(Component title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File[] showOpenMultiFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File[] showOpenMultiFileDialog(Component title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showSaveFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showSaveFileDialog(Component title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);
}
