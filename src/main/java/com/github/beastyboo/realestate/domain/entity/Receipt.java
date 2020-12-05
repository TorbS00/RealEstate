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
        private UUID id = UUID.randomUUID();
        private final double price;
        private final String date;

        public Builder(UUID buyer, UUID seller, double price, String date) {
            this.buyer = buyer;
            this.seller = seller;
            this.price = price;
            this.date = date;
        }

        public Builder id (UUID val) {
            id = val;
            return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (Double.compare(receipt.getPrice(), getPrice()) != 0) return false;
        if (!getBuyer().equals(receipt.getBuyer())) return false;
        if (!getSeller().equals(receipt.getSeller())) return false;
        if (!getId().equals(receipt.getId())) return false;
        return getDate().equals(receipt.getDate());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getBuyer().hashCode();
        result = 31 * result + getSeller().hashCode();
        result = 31 * result + getId().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getDate().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", date='" + date + '\'' +
                '}';
    }
}
