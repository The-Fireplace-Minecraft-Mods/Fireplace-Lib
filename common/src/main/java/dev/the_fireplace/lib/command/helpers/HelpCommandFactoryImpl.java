package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.command.injectables.HelpCommandFactory;
import dev.the_fireplace.lib.api.command.interfaces.HelpCommand;
import net.minecraft.commands.CommandSourceStack;

import javax.inject.Singleton;

@Implementation
@Singleton
public final class HelpCommandFactoryImpl implements HelpCommandFactory
{
    @Override
    public HelpCommand create(String modid, LiteralArgumentBuilder<CommandSourceStack> helpCommandBase) {
        return new HelpCommandImpl(modid, helpCommandBase);
    }
}
