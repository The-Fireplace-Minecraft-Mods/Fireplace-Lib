package dev.the_fireplace.lib.api.client.io;

import dev.the_fireplace.lib.impl.io.FileDialogFactoryImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import javax.annotation.Nullable;
import java.io.File;

@Environment(EnvType.CLIENT)
public interface FileDialogFactory {
    static FileDialogFactory getInstance() {
        //noinspection deprecation
        return FileDialogFactoryImpl.INSTANCE;
    }

    @Nullable
    File showOpenFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showOpenFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File[] showOpenMultiFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File[] showOpenMultiFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showSaveFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showSaveFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);
}
