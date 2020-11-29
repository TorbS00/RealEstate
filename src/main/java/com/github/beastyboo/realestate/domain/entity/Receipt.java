package com.github.beastyboo.realestate.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class Receipt {

    private final UUID buyer;
    private final UUID seller;
    private final UUID id;
    private final double price;
    private final LocalDate date;

    public static class Builder {
        private final UUID buyer;
        private final UUID seller;
        private final UUID id;
        private final double price;
        private final LocalDate date;

        public Builder(UUID buyer, UUID seller, UUID id, double price, LocalDate date) {
            this.buyer = buyer;
            this.seller = seller;
            this.id = id;
            this.price = price;
            this.date = date;
        }

        private Receipt build() {
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

    public LocalDate getDate() {
        return date;
    }
}
