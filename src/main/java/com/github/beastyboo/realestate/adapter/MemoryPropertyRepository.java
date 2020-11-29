package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.port.PropertyRepository;
import me.ryanhamshire.GriefPrevention.Claim;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Torbie on 29.11.2020.
 */
public class MemoryPropertyRepository implements PropertyRepository {

    private final RealEstate core;
    private final Map<UUID, Property> propertyMemory;
    private final Economy econ;

    public MemoryPropertyRepository(RealEstate core) {
        this.core = core;
        propertyMemory = new HashMap<>();
        econ = core.getEconomy();
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
    public boolean createProperty(String name, Player player, Location location, double price) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(property.isPresent()) {
            return false;
        }

        Claim claim = core.getGriefPrevention().getClaimAt(location, true, null);

        if(claim.ownerID != player.getUniqueId()) {
            return false;
        }

        Property newProperty = new Property.Builder(name, claim.ownerID, claim, price).build();
        propertyMemory.put(newProperty.getId(), newProperty);
        return true;
    }

    @Override
    public boolean deleteProperty(Player player, Location location) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(!property.isPresent()) {
            return false;
        }

        if(player.getUniqueId() != property.get().getSeller() || !player.isOp()) {
            return false;
        }

        propertyMemory.remove(property.get().getId(), property.get());
        return false;
    }

    @Override
    public boolean buyProperty(Player player, Location location) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(!property.isPresent()) {
            return false;
        }

        if(player.getUniqueId() == property.get().getSeller()) {
            return false;
        }

        double price = property.get().getPrice();

        if(econ.getBalance(player) < price) {
            return false;
        }

        econ.withdrawPlayer(player, price);
        econ.depositPlayer(Bukkit.getOfflinePlayer(property.get().getSeller()), price);
        property.get().getClaim().allowAccess(player);

        propertyMemory.remove(property.get().getId(), property.get());
        return true;
    }


    @Override
    public Optional<Property> getPropertyByID(UUID uuid) {
        return Optional.ofNullable(propertyMemory.get(uuid));
    }

    @Override
    public Optional<Property> getPropertyByLocation(Location location) {
        Claim claim = core.getGriefPrevention().getClaimAt(location, true, null);
        if(claim == null) {
            return Optional.empty();
        }
        for(Property property : propertyMemory.values()) {
            if (claim.equals(property.getClaim())) {
                return Optional.of(property);
            }
        }
        return Optional.empty();
    }

    @Override
    public Set<Property> getAllProperties() {
        return new HashSet<>(propertyMemory.values());
    }

}
