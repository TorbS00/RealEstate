package com.github.beastyboo.realestate.domain.entity;

import me.ryanhamshire.GriefPrevention.Claim;

import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class Property {

    private final String name;
    private final UUID seller;
    private final UUID id;
    private final Claim claim;
    private final double price;

    public static class Builder {
        private final String name;
        private final UUID seller;
        private final UUID id = UUID.randomUUID();
        private final Claim claim;
        private final double price;

        public Builder(String name, UUID seller, Claim claim, double price) {
            this.name = name;
            this.seller = seller;
            this.claim = claim;
            this.price = price;
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
}
