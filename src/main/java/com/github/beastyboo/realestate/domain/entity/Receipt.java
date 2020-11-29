package com.github.beastyboo.realestate.domain.entity;

import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class Receipt {

    private final UUID buyer;
    private final UUID seller;
    private final UUID id;
    private final double price;
    private final String date;

    public static class Builder {
        private final UUID buyer;
        private final UUID seller;
        private final UUID id = UUID.randomUUID();
        private final double price;
        private final String date;

        public Builder(UUID buyer, UUID seller, double price, String date) {
            this.buyer = buyer;
            this.seller = seller;
            this.price = price;
            this.date = date;
        }

        public Receipt build() {
            return new Receipt(this);
        }

    }

    private Receipt(Builder builder) {
        buyer = builder.buyer;
        seller = builder.seller;
        id = builder.id;
        price = builder.price;
        date = builder.date;
    }

    public UUID getBuyer() {
        return buyer;
    }

    public UUID getSeller() {
        return seller;
    }

    public UUID getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
