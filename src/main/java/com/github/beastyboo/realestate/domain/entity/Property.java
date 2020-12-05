package com.github.beastyboo.realestate.domain.entity;

import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class Property {

    private final String name;
    private final UUID seller;
    private final UUID id;
    private final Claim claim;
    private double price;
    private final Location location;

    public static class Builder {
        private final String name;
        private final UUID seller;
        private UUID id = UUID.randomUUID();
        private final Claim claim;
        private final double price;
        private final Location location;

        public Builder(String name, UUID seller, Claim claim, double price, Location location) {
            this.name = name;
            this.seller = seller;
            this.claim = claim;
            this.price = price;
            this.location = location;
        }

        public Builder id (UUID val) {
            id = val;
            return this;
        }

        public Property build() {
            return new Property(this);
        }
    }

    private Property(Builder builder) {
        name = builder.name;
        seller = builder.seller;
        id = builder.id;
        claim = builder.claim;
        price = builder.price;
        location = builder.location;
    }

    public String getName() {
        return name;
    }

    public UUID getSeller() {
        return seller;
    }

    public UUID getId() {
        return id;
    }

    public Claim getClaim() {
        return claim;
    }

    public double getPrice() {
        return price;
    }

    public Location getLocation() {
        return location;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        if (Double.compare(property.getPrice(), getPrice()) != 0) return false;
        if (!getName().equals(property.getName())) return false;
        if (!getSeller().equals(property.getSeller())) return false;
        if (!getId().equals(property.getId())) return false;
        if (!getClaim().equals(property.getClaim())) return false;
        return getLocation().equals(property.getLocation());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        result = 31 * result + getSeller().hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + getClaim().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getLocation().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", price=" + price +
                '}';
    }
}
