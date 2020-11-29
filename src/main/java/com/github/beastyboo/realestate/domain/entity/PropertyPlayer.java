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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyPlayer that = (PropertyPlayer) o;

        if (!getUuid().equals(that.getUuid())) return false;
        if (!getProperties().equals(that.getProperties())) return false;
        return getReceipts().equals(that.getReceipts());
    }

    @Override
    public int hashCode() {
        int result = getUuid().hashCode();
        result = 31 * result + getProperties().hashCode();
        result = 31 * result + getReceipts().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PropertyPlayer{" +
                "uuid=" + uuid +
                '}';
    }
}
