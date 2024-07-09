package dev.the_fireplace.lib.command.helpers;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.injectables.TextPaginator;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.interfaces.HelpCommand;
import dev.the_fireplace.lib.chat.translation.TranslatorManager;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Function;

public final class HelpCommandImpl implements HelpCommand
{
    private static final Function<String, Collection<String>> NEW_CHILD_NODE_SET = unused -> new HashSet<>(2);
    private final TextStyles textStyles;
    private final TextPaginator textPaginator;
    private final Translator translator;
    private final String modId;
    private final LiteralArgumentBuilder<CommandSourceStack> helpCommandBase;
    private final Map<String, Collection<String>> commands = new HashMap<>();
    private final IntSet grandchildNodeHashes = new IntArraySet(3);

    HelpCommandImpl(String modId, LiteralArgumentBuilder<CommandSourceStack> helpCommandBase) {
        this.modId = modId;
        this.helpCommandBase = helpCommandBase;
        Injector injector = FireplaceLibConstants.getInjector();
        this.translator = injector.getInstance(TranslatorManager.class).getTranslator(modId);
        this.textStyles = injector.getInstance(TextStyles.class);
        this.textPaginator = injector.getInstance(TextPaginator.class);
    }

    @Override
    public HelpCommand addCommands(CommandNode<?>... commands) {
        String[] commandNames = Arrays.stream(commands).map(CommandNode::getName).toArray(String[]::new);

        return addCommands(commandNames);
    }

    @Override
    public HelpCommand addCommands(String... commands) {
        for (String command : commands) {
            this.commands.putIfAbsent(command, Collections.emptySet());
        }
        this.commands.putIfAbsent(helpCommandBase.getLiteral(), Collections.emptySet());

        return this;
    }

    @Override
    public HelpCommand addSubCommandsFromCommands(CommandNode<?>... commands) {
        for (CommandNode<?> node : commands) {
            for (Iterator<? extends CommandNode<?>> it = node.getChildren().stream().sorted().iterator(); it.hasNext(); ) {
                CommandNode<?> child = it.next();
                int childPathHash = buildChildPathHash(new StringBuilder(), child);
                if (isNewChild(child, childPathHash)) {
                    this.commands.computeIfAbsent(node.getName(), NEW_CHILD_NODE_SET).add(child.getName());
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
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(helpCommandBase
            .executes((command) -> runHelpCommand(command, 1))
            .then(Commands.argument("page", IntegerArgumentType.integer(1))
                .executes((command) -> runHelpCommand(command, command.getArgument("page", Integer.class)))
            )
        );
    }

    private int runHelpCommand(CommandContext<CommandSourceStack> command, int page) {
        textPaginator.sendPaginatedChat(command.getSource(), "/" + helpCommandBase.getLiteral() + " %s", getHelpsList(command), page);
        return Command.SINGLE_SUCCESS;
    }

    private List<? extends Component> getHelpsList(CommandContext<CommandSourceStack> command) {
        List<Component> helpComponents = Lists.newArrayList();
        for (Map.Entry<String, Collection<String>> commandName : commands.entrySet()) {
            if (commandName.getValue().isEmpty()) {
                Component commandHelp = buildCommandDescription(command, commandName.getKey());
                helpComponents.add(commandHelp);
            } else {
                for (String subCommand : commandName.getValue()) {
                    Component commandHelp = buildSubCommandDescription(command, commandName.getKey(), subCommand);
                    helpComponents.add(commandHelp);
                }
            }
        }
        helpComponents.sort(Comparator.comparing(Component::getString));

        int i = 0;
        for (Component helpComponent : helpComponents) {
            helpComponent.setStyle(i++ % 2 == 0 ? textStyles.white() : textStyles.grey());
        }

        return helpComponents;
    }

    private Component buildCommandDescription(CommandContext<CommandSourceStack> command, String commandName) {
        return translator.getTextForTarget(command.getSource(), "commands." + modId + "." + commandName + ".usage")
            .append(": ")
            .append(translator.getTextForTarget(command.getSource(), "commands." + modId + "." + commandName + ".description"));
    }

    private Component buildSubCommandDescription(CommandContext<CommandSourceStack> command, String commandName, String subCommand) {
        return translator.getTextForTarget(command.getSource(), "commands." + modId + "." + commandName + "." + subCommand + ".usage")
            .append(": ")
            .append(translator.getTextForTarget(command.getSource(), "commands." + modId + "." + commandName + "." + subCommand + ".description"));
    }
}
