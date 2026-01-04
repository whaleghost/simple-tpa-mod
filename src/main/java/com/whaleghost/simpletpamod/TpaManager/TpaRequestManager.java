package com.whaleghost.simpletpamod.TpaManager;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TpaRequestManager {

    private static final Map<UUID, TpaRequest> pendingRequests = new ConcurrentHashMap<>();
    private static final long REQUEST_EXPIRATION_TIME = 60000;

    private static final String PLAYER_NOT_FOUND   = "The specified player does not exist.";
    private static final String REQUEST_TIMEOUT    = "Request has been expired.";
    private static final String REQUEST_NOT_FOUND  = "You haven't got a suspended request.";

    public static void distributeTpaRequest(ServerPlayer sender, ServerPlayer receiver) {
        if (receiver == null) {
            sender.sendSystemMessage(Component.literal(PLAYER_NOT_FOUND));
        } else {
            String senderMsg = String.format("Request has been sent to %s.", receiver.getGameProfile().getName());
            String receiverMsg = String.format(
                    "%s wants to teleport to your location, use /tpaccpet to accept, otherwise use /tpdeny.",
                    sender.getGameProfile().getName()
            );

            sender.sendSystemMessage(Component.literal(senderMsg));
            receiver.sendSystemMessage(Component.literal(receiverMsg));

            pendingRequests.put(receiver.getUUID(),
                    new TpaRequest(sender.getUUID(), receiver.getUUID(), System.currentTimeMillis())
            );
        }
    }

    public static void distributeTpaHereRequest(ServerPlayer sender, ServerPlayer receiver) {
        if (receiver == null) {
            sender.sendSystemMessage(Component.literal(PLAYER_NOT_FOUND));
        } else {
            String senderMsg = String.format("Request has been sent to %s.", receiver.getGameProfile().getName());
            String receiverMsg = String.format(
                    "%s wants you to teleport to %s's location, use /tpaccpet to accept, otherwise use /tpdeny.",
                    sender.getGameProfile().getName(),
                    sender.getGameProfile().getName()
            );

            sender.sendSystemMessage(Component.literal(senderMsg));
            receiver.sendSystemMessage(Component.literal(receiverMsg));

            pendingRequests.put(receiver.getUUID(),
                    new TpaRequest(receiver.getUUID(), sender.getUUID(), System.currentTimeMillis())
            );
        }
    }

    public static void executeTpaTeleport(ServerPlayer sender, ServerPlayer target) {
        if (sender == null) {
            return;
        }

        teleportPlayerTo(sender, target);

        String senderMsg = String.format("You have been teleported to %s.", target.getGameProfile().getName());
        String receiverMsg = String.format("%s has been teleported to you.", sender.getGameProfile().getName());
        sender.sendSystemMessage(Component.literal(senderMsg));
        target.sendSystemMessage(Component.literal(receiverMsg));
    }

    public static void teleportPlayerTo(ServerPlayer player, ServerPlayer target) {
        if (player == null || target == null) return;

        ServerLevel targetLevel = target.serverLevel();
        double x = target.getX();
        double y = target.getY();
        double z = target.getZ();
        float yaw = target.getYRot();
        float pitch = target.getXRot();

        player.teleportTo(targetLevel, x, y, z, yaw, pitch);
    }

}
