package com.github.beastyboo.realestate.adapter.typeadapter;

import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.entry.RealEstateAPI;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 05.12.2020.
 */
public class PropertyPlayerTypeAdapter extends TypeAdapter<PropertyPlayer>{

    @Override
    public void write(JsonWriter out, PropertyPlayer value) throws IOException {
        out.beginObject();

        out.name("uuid").value(value.getUuid().toString());

        out.name("properties").beginArray();
        for(Property property : value.getProperties()) {
            out.beginObject();
            out.name("uuid").value(property.getId().toString());
            out.endObject();
        }
        out.endArray();

        out.name("receipts").beginArray();
        for(Receipt receipt : value.getReceipts()) {
            out.beginObject();
            out.name("uuid").value(receipt.getId().toString());
            out.endObject();
        }
        out.endArray();

        out.endObject();
    }

    @Override
    public PropertyPlayer read(JsonReader in) throws IOException {
        in.beginObject();

        UUID uuid = null;
        final Set<Property> properties = new HashSet<>();
        final Set<Receipt> receipts = new HashSet<>();

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "uuid":
                    uuid = UUID.fromString(in.nextString());
                    break;
                case "properties":
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginObject();
                        while(in.hasNext()) {
                            switch (in.nextName()) {
                                case "uuid":
                                    properties.add(RealEstateAPI.getINSTANCE().getPropertyByID(UUID.fromString(in.nextString())).get());
                                    break;
                            }
                        }
                        in.endObject();
                    }
                    in.endArray();
                    break;
                case "receipts":
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginObject();
                        while(in.hasNext()) {
                            switch (in.nextName()) {
                                case "uuid":
                                    receipts.add(RealEstateAPI.getINSTANCE().getReceipt(UUID.fromString(in.nextString())).get());
                                    break;
                            }
                        }
                        in.endObject();
                    }
                    in.endArray();
                    break;
            }
        }
        PropertyPlayer propertyPlayer = new PropertyPlayer(uuid, properties, receipts);
        in.endObject();
        return propertyPlayer;
    }
}
