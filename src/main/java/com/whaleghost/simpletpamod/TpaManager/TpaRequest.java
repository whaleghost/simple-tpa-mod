package com.whaleghost.simpletpamod.TpaManager;

import java.util.UUID;

public class TpaRequest {

    private final UUID player;
    private final UUID target;
    private final long timestamp;

    public TpaRequest(UUID player, UUID target, long timestamp) {
        this.player = player;
        this.target = target;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getPlayer() {
        return player;
    }

    public boolean isExpired(long maxTime) {
        return System.currentTimeMillis() - timestamp > maxTime;
    }
}
