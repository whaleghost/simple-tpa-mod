package com.whaleghost.simpletpamod.TpaManager;

import java.util.UUID;

public class TpaRequest {

    private final UUID from;
    private final UUID target;
    private final long timestamp;

    public TpaRequest(UUID player, UUID target, long timestamp) {
        this.from = player;
        this.target = target;
        this.timestamp = timestamp;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getFrom() {
        return from;
    }

    public boolean isExpired(long maxTime) {
        return System.currentTimeMillis() - timestamp > maxTime;
    }
}
