package com.github.beastyboo.realestate.adapter.typeadapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Torbie on 05.12.2020.
 */
public class PropertyTypeAdapter extends TypeAdapter<Property>{

    private final RealEstate core;

    public PropertyTypeAdapter(RealEstate core) {
        this.core = core;
    }

    @Override
    public void write(JsonWriter out, Property value) throws IOException {
        out.beginObject();

        out.name("name").value(value.getName());
        out.name("seller").value(value.getSeller().toString());
        out.name("id").value(value.getId().toString());
        out.name("claim").value(value.getClaim().getID());
        out.name("price").value(value.getPrice());

        out.name("world").value(value.getLocation().getWorld().getName());
        out.name("x").value(value.getLocation().getX());
        out.name("y").value(value.getLocation().getY());
        out.name("z").value(value.getLocation().getZ());
        out.name("yaw").value(value.getLocation().getYaw());
        out.name("pitch").value(value.getLocation().getPitch());

        out.endObject();
    }

    @Override
    public Property read(JsonReader in) throws IOException {
        in.beginObject();

        String name = "";
        UUID seller = null;
        UUID id = null;
        Claim claim = null;
        double price = 0;

        World world = null;
        double x = 0;
        double y = 0;
        double z = 0;
        double yaw = 0;
        double pitch = 0;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "name":
                    name = in.nextString();
                    break;
                case "seller":
                    seller = UUID.fromString(in.nextString());
                    break;
                case "id":
                    id = UUID.fromString(in.nextString());
                    break;
                case "claim":
                    claim = core.getGriefPrevention().getClaim(in.nextLong());
                    break;
                case "price":
                    price = in.nextDouble();
                    break;
                case "world":
                    world = Bukkit.getWorld(in.nextString());
                    break;
                case "x":
                    x = in.nextDouble();
                    break;
                case "y":
                    y = in.nextDouble();
                    break;
                case "z":
                    z = in.nextDouble();
                    break;
                case "yaw":
                    yaw = in.nextDouble();
                    break;
                case "pitch":
                    pitch = in.nextDouble();
                    break;
            }
        }

        Location location = new Location(world, x, y, z, (float) yaw, (float) pitch);
        Property property = new Property.Builder(name, seller, claim, price, location).id(id).build();
        in.endObject();
        return property;
    }
}
