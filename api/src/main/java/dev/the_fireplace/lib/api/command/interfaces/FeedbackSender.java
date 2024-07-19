package dev.the_fireplace.lib.api.command.interfaces;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public interface FeedbackSender
{
    int throwFailure(CommandContext<CommandSourceStack> command, String translationKey, Object... args) throws CommandSyntaxException;

    void basic(CommandContext<CommandSourceStack> command, String translationKey, Object... args);

    void basic(ServerPlayer targetPlayer, String translationKey, Object... args);

    void styled(CommandContext<CommandSourceStack> command, Style style, String translationKey, Object... args);

    void styled(ServerPlayer targetPlayer, Style style, String translationKey, Object... args);
}
