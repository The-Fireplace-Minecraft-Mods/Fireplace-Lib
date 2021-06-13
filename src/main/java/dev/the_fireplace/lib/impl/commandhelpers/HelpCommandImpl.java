package dev.the_fireplace.lib.impl.commandhelpers;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.the_fireplace.annotateddi.AnnotatedDI;
import dev.the_fireplace.lib.api.chat.injectables.TextPaginator;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.interfaces.HelpCommand;
import dev.the_fireplace.lib.impl.translation.TranslatorManager;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.*;

public final class HelpCommandImpl implements HelpCommand {
    private final TextStyles textStyles = AnnotatedDI.getInjector().getInstance(TextStyles.class);
    private final TextPaginator textPaginator = AnnotatedDI.getInjector().getInstance(TextPaginator.class);
    private final Translator translator;
    private final String modid;
    private final LiteralArgumentBuilder<ServerCommandSource> helpCommandBase;
    private final Map<String, Collection<String>> commands = new HashMap<>();
    private final IntSet grandchildNodeHashes = new IntArraySet(3);

    HelpCommandImpl(String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase) {
        this.modid = modid;
        this.helpCommandBase = helpCommandBase;
        this.translator = AnnotatedDI.getInjector().getInstance(TranslatorManager.class).getTranslator(modid);
    }

    @Override
    public HelpCommand addCommands(CommandNode<?>... commands) {
        String[] commandNames = Arrays.stream(commands).map(CommandNode::getName).toArray(String[]::new);

        return addCommands(commandNames);
    }

    @Override
    public HelpCommand addCommands(String... commands) {
        for (String command: commands) {
            this.commands.putIfAbsent(command, Collections.emptySet());
        }
        this.commands.putIfAbsent(helpCommandBase.getLiteral(), Collections.emptySet());

        return this;
    }

    @Override
    public HelpCommand addSubCommandsFromCommands(CommandNode<?>... commands) {
        for (CommandNode<?> node: commands) {
            for (Iterator<? extends CommandNode<?>> it = node.getChildren().stream().sorted().iterator(); it.hasNext();) {
                CommandNode<?> child = it.next();
                int childPathHash = buildChildPathHash(new StringBuilder(), child);
                if (isNewChild(child, childPathHash)) {
                    this.commands.computeIfAbsent(node.getName(), n -> new HashSet<>(2)).add(child.getName());
                    this.grandchildNodeHashes.add(childPathHash);
                }
            }
        }
        this.commands.putIfAbsent(helpCommandBase.getLiteral(), Collections.emptySet());

        return this;
    }

    private boolean isNewChild(CommandNode<?> child, int childPathHash) {
        return child instanceof LiteralCommandNode && !grandchildNodeHashes.contains(childPathHash);
    }

    private int buildChildPathHash(StringBuilder stringBuilder, CommandNode<?> node) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(node.getName()).append("->");
        } else {
            stringBuilder.append("root->");
        }
        Optional<? extends CommandNode<?>> firstGrandchild = node.getChildren().stream().sorted().findFirst();
        //noinspection OptionalIsPresent
        if (!firstGrandchild.isPresent()) {
            return stringBuilder.append(node.getCommand().toString()).toString().hashCode();
        } else {
            return buildChildPathHash(stringBuilder, firstGrandchild.get());
        }
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(helpCommandBase
            .executes((command) -> runHelpCommand(command, 1))
            .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                .executes((command) -> runHelpCommand(command, command.getArgument("page", Integer.class)))
            )
        );
    }

    private int runHelpCommand(CommandContext<ServerCommandSource> command, int page) {
        textPaginator.sendPaginatedChat(command.getSource(), "/" + helpCommandBase.getLiteral() + " %s", getHelpsList(command), page);
        return Command.SINGLE_SUCCESS;
    }

    private List<? extends Text> getHelpsList(CommandContext<ServerCommandSource> command) {
        List<Text> helps = Lists.newArrayList();
        for (Map.Entry<String, Collection<String>> commandName: commands.entrySet()) {
            if (commandName.getValue().isEmpty()) {
                Text commandHelp = buildCommandDescription(command, commandName.getKey());
                helps.add(commandHelp);
            } else {
                for (String subCommand: commandName.getValue()) {
                    Text commandHelp = buildSubCommandDescription(command, commandName.getKey(), subCommand);
                    helps.add(commandHelp);
                }
            }
        }
        helps.sort(Comparator.comparing(Text::getString));

        int i = 0;
        for (Text helpText: helps) {
            helpText.setStyle(i++ % 2 == 0 ? textStyles.white() : textStyles.grey());
        }

        return helps;
    }

    private Text buildCommandDescription(CommandContext<ServerCommandSource> command, String commandName) {
        return translator.getTextForTarget(command.getSource(), "commands." + modid + "." + commandName + ".usage")
            .append(": ")
            .append(translator.getTextForTarget(command.getSource(), "commands." + modid + "." + commandName + ".description"));
    }

    private Text buildSubCommandDescription(CommandContext<ServerCommandSource> command, String commandName, String subCommand) {
        return translator.getTextForTarget(command.getSource(), "commands." + modid + "." + commandName + "." + subCommand + ".usage")
            .append(": ")
            .append(translator.getTextForTarget(command.getSource(), "commands." + modid + "." + commandName + "." + subCommand + ".description"));
    }
}
