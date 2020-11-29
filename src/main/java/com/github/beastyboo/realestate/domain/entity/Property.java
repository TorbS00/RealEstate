package com.github.beastyboo.realestate.domain.entity;

import me.ryanhamshire.GriefPrevention.Claim;

import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class Property {

    private final UUID seller;
    private final UUID id;
    private final Claim claim;
    private final double price;

    public static class Builder {
        private final UUID seller;
        private final UUID id = UUID.randomUUID();
        private final Claim claim;
        private final double price;

        public Builder(UUID seller, Claim claim, double price) {
            this.seller = seller;
            this.claim = claim;
            this.price = price;
        }

        public Property build() {
            return new Property(this);
        }
    }

    private Property(Builder builder) {
        seller = builder.seller;
        id = builder.id;
        claim = builder.claim;
        price = builder.price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        if (Double.compare(property.getPrice(), getPrice()) != 0) return false;
        if (!getSeller().equals(property.getSeller())) return false;
        if (!getId().equals(property.getId())) return false;
        return getClaim().equals(property.getClaim());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getSeller().hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + getClaim().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Property{" +
                "seller=" + seller +
                ", id=" + id +
                ", claim=" + claim.getID() +
                ", price=" + price +
                '}';
    }
}
