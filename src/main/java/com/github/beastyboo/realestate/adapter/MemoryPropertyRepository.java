package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.port.PropertyRepository;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Torbie on 29.11.2020.
 */
public class MemoryPropertyRepository implements PropertyRepository {

    private final RealEstate core;
    private final Map<UUID, Property> propertyMemory;

    public MemoryPropertyRepository(RealEstate core) {
        this.core = core;
        propertyMemory = new HashMap<>();
    }


    @Override
    public void load() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean displayGUI(Player player) {
        return false;
    }

    @Override
    public boolean createProperty(UUID player, UUID id, Claim claim, double price) {
        return false;
    }

    @Override
    public boolean deleteProperty(Property property) {
        return false;
    }

    @Override
    public boolean buyProperty(Property property) {
        return false;
    }

    @Override
    public Optional<Property> getPropertyByID(UUID uuid) {
        return null;
    }

    @Override
    public Optional<Property> getPropertyByLocation(Location location) {
        return null;
    }

    @Override
    public Set<Property> getAllProperties() {
        return null;
    }

}
