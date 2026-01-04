package com.whaleghost.simpletpamod.TpaCommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.whaleghost.simpletpamod.TpaManager.TpaRequestManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class TpaOptionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpaccept")
                        .executes(TpaOptionCommand::acceptRequest)
        );
        dispatcher.register(
                Commands.literal("tpdeny")
                        .executes(TpaOptionCommand::denyRequest)
        );
    }

    private static int acceptRequest(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer judger = source.getPlayer();

        TpaRequestManager.acceptRequest(judger);

        return Command.SINGLE_SUCCESS;
    }

    private static int denyRequest(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer judger = source.getPlayer();

        TpaRequestManager.denyRequest(judger);

        return Command.SINGLE_SUCCESS;
    }

}
