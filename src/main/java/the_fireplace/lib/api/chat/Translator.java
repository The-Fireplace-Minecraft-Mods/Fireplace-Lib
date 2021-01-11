package the_fireplace.lib.api.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.UUID;

public interface Translator {
    Text getTextForTarget(ServerCommandSource target, String translationKey, Object... args);

    Text getTextForTarget(CommandOutput target, String translationKey, Object... args);

    Text getTextForTarget(UUID target, String translationKey, Object... args);

    LiteralText getTranslatedText(String translationKey, Object... args);

    String getTranslatedString(String translationKey, Object... args);

    String getTranslationKeyForTarget(CommandOutput target, String translationKey);

    String getTranslationKeyForTarget(UUID target, String translationKey);
}
