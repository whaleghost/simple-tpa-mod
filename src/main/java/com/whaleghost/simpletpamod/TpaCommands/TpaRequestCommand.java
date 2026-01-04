package com.whaleghost.simpletpamod.TpaCommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.whaleghost.simpletpamod.TpaManager.TpaRequestManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class TpaRequestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("tpa")
                    .then(
                        Commands.argument("player", EntityArgument.player())
                                .executes(TpaRequestCommand::sendTpaRequest)
                    )
                    .executes(context -> Command.SINGLE_SUCCESS)
        );
        dispatcher.register(
            Commands.literal("tpahere")
                    .then(
                        Commands.argument("player", EntityArgument.player())
                                .executes(TpaRequestCommand::sendTpaHereRequest)
                    )
                    .executes(context -> Command.SINGLE_SUCCESS)
        );
    }

    private static int sendTpaRequest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer sender = source.getPlayer();
        ServerPlayer receiver = EntityArgument.getPlayer(context, "player");

        TpaRequestManager.distributeTpaRequest(sender, receiver);

        return Command.SINGLE_SUCCESS;
    }

    private static int sendTpaHereRequest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer sender = source.getPlayer();
        ServerPlayer receiver = EntityArgument.getPlayer(context, "player");

        TpaRequestManager.distributeTpaHereRequest(sender, receiver);

        return Command.SINGLE_SUCCESS;
    }

}

