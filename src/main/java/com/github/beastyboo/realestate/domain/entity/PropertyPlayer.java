package com.github.beastyboo.realestate.domain.entity;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class PropertyPlayer {

    private final UUID uuid;
    private final Set<Property> properties;
    private final Set<Receipt> receipts;

    public PropertyPlayer(UUID uuid, Set<Property> properties, Set<Receipt> receipts) {
        this.uuid = uuid;
        this.properties = properties;
        this.receipts = receipts;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Set<Receipt> getReceipts() {
        return receipts;
    }
}
