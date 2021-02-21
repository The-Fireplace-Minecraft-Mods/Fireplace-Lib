package dev.the_fireplace.lib.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.api.chat.TranslatorManager;
import dev.the_fireplace.lib.api.command.FeedbackSender;
import dev.the_fireplace.lib.api.command.FeedbackSenderManager;
import dev.the_fireplace.lib.api.command.Requirements;
import dev.the_fireplace.lib.impl.FireplaceLib;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public final class FLCommands {
    public static void register(MinecraftServer server) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        Requirements requirements = Requirements.getInstance();
        Translator translator = TranslatorManager.getInstance().getTranslator(FireplaceLib.MODID);
        FeedbackSender feedbackSender = FeedbackSenderManager.getInstance().get(translator);

        new FLReloadCommand(requirements, feedbackSender).register(dispatcher);
    }
}
