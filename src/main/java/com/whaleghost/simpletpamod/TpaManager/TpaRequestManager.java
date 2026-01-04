package com.whaleghost.simpletpamod.TpaManager;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TpaRequestManager {

    private static final Map<UUID, TpaRequest> pendingRequests = new ConcurrentHashMap<>();
    private static final long REQUEST_EXPIRATION_TIME = 60000;

    private static final String PLAYER_NOT_FOUND   = "[TPA]The specified player does not exist(may be offline).";
    private static final String REQUEST_TIMEOUT    = "[TPA]Request has been expired.";
    private static final String REQUEST_NOT_FOUND  = "[TPA]You haven't got a pending request.";

    public static void distributeTpaRequest(ServerPlayer sender, ServerPlayer receiver) {
        if (receiver == null) {
            sender.sendSystemMessage(Component.literal(PLAYER_NOT_FOUND).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
        } else {
            String senderMsg = String.format(
                    "[TPA]Request: %s->%s, has been sent.",
                    sender.getGameProfile().getName(),
                    receiver.getGameProfile().getName()
            );
            String receiverMsg = String.format(
                    "[TPA]Request: %s->%s, /tpaccpet √, /tpdeny ×, available in 1min.",
                    sender.getGameProfile().getName(),
                    receiver.getGameProfile().getName()
            );

            sender.sendSystemMessage(Component.literal(senderMsg).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00))));
            receiver.sendSystemMessage(Component.literal(receiverMsg).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00))));

            pendingRequests.put(receiver.getUUID(),
                    new TpaRequest(sender.getUUID(), receiver.getUUID(), System.currentTimeMillis())
            );
        }
    }

    public static void distributeTpaHereRequest(ServerPlayer sender, ServerPlayer receiver) {
        if (receiver == null) {
            sender.sendSystemMessage(Component.literal(PLAYER_NOT_FOUND).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
        } else {
            String senderMsg = String.format(
                    "[TPA]Request: %s->%s, has been sent.",
                    receiver.getGameProfile().getName(),
                    sender.getGameProfile().getName()
            );
            String receiverMsg = String.format(
                    "[TPA]Request: %s->%s, /tpaccpet √, /tpdeny ×, available in 1min.",
                    receiver.getGameProfile().getName(),
                    sender.getGameProfile().getName()
            );

            sender.sendSystemMessage(Component.literal(senderMsg).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00))));
            receiver.sendSystemMessage(Component.literal(receiverMsg).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00))));

            pendingRequests.put(receiver.getUUID(),
                    new TpaRequest(receiver.getUUID(), sender.getUUID(), System.currentTimeMillis())
            );
        }
    }

    public static void acceptRequest(ServerPlayer judger) {
        TpaRequest request = fetchRequest(judger);
        if (request == null) {
            judger.sendSystemMessage(Component.literal(REQUEST_NOT_FOUND).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
            return;
        }
        if (request.isExpired(REQUEST_EXPIRATION_TIME)) {
            judger.sendSystemMessage(Component.literal(REQUEST_TIMEOUT).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
            return;
        }
        ServerPlayer from = judger.getServer().getPlayerList().getPlayer(request.getFrom());
        ServerPlayer target = judger.getServer().getPlayerList().getPlayer(request.getTarget());
        if (target == null) {
            return;
        }
        executeTeleport(from, target);
    }

    public static void denyRequest(ServerPlayer judger) {
        TpaRequest request = fetchRequest(judger);
        ServerPlayer from = judger.getServer().getPlayerList().getPlayer(request.getFrom());
        ServerPlayer target = judger.getServer().getPlayerList().getPlayer(request.getTarget());
        String message = String.format(
                "[TPA]Request: %s->%s, denied.",
                from.getGameProfile().getName(),
                target.getGameProfile().getName()
        );
        if (from != null) {
            from.sendSystemMessage(Component.literal(message).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
        }
        if (target != null) {
            target.sendSystemMessage(Component.literal(message).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
        }
    }

    public static void executeTeleport(ServerPlayer from, ServerPlayer target) {
        teleportPlayerTo(from, target);

        String message = String.format(
                "[TPA]Request: %s->%s, accepted.",
                from.getGameProfile().getName(),
                target.getGameProfile().getName()
        );
        from.sendSystemMessage(Component.literal(message).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00))));
        target.sendSystemMessage(Component.literal(message).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00))));
    }

    public static void teleportPlayerTo(ServerPlayer from, ServerPlayer target) {
        if (from != null && target != null) {
            ServerLevel targetLevel = target.serverLevel();
            double x = target.getX();
            double y = target.getY();
            double z = target.getZ();
            float yaw = target.getYRot();
            float pitch = target.getXRot();

            from.teleportTo(targetLevel, x, y, z, yaw, pitch);
        } else {
            if (from != null) {
                from.sendSystemMessage(
                        Component.literal(PLAYER_NOT_FOUND)
                                 .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)))
                );
            }
            if (target != null) {
                target.sendSystemMessage(
                        Component.literal(PLAYER_NOT_FOUND)
                                 .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)))
                );
            }
        }
    }

    public static TpaRequest fetchRequest(ServerPlayer judger) {
        return pendingRequests.remove(judger.getUUID());
    }

}
