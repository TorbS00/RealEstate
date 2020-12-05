package com.github.beastyboo.realestate.adapter.typeadapter;

import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Torbie on 05.12.2020.
 */
public class ReceiptTypeAdapter extends TypeAdapter<Receipt>{

    @Override
    public void write(JsonWriter out, Receipt value) throws IOException {
        out.beginObject();

        out.name("buyer").value(value.getBuyer().toString());
        out.name("seller").value(value.getSeller().toString());
        out.name("id").value(value.getId().toString());
        out.name("price").value(value.getPrice());
        out.name("date").value(value.getDate());

        out.endObject();
    }

    @Override
    public Receipt read(JsonReader in) throws IOException {
        in.beginObject();

        UUID buyer = null;
        UUID seller = null;
        UUID id = null;
        double price = 0;
        String date = " ";

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "buyer":
                    buyer = UUID.fromString(in.nextString());
                    break;
                case "seller":
                    seller = UUID.fromString(in.nextString());
                    break;
                case "id":
                    id = UUID.fromString(in.nextString());
                    break;
                case "price":
                    price = in.nextDouble();
                    break;
                case "date":
                    date = in.nextString();
                    break;
            }
        }

        Receipt receipt = new Receipt.Builder(buyer, seller, price, date).id(id).build();
        in.endObject();
        return receipt;
    }
}
