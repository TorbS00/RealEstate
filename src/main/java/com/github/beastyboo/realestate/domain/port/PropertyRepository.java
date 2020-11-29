package com.github.beastyboo.realestate.domain.port;

import com.github.beastyboo.realestate.domain.entity.Property;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public interface PropertyRepository {

    void load();

    void close();

    boolean displayGUI(Player player);

    boolean createProperty(UUID player, UUID id, Claim claim, double price);

    boolean deleteProperty(Property property);

    boolean buyProperty(Property property);

    Optional<Property> getPropertyByID(UUID uuid);

    Optional<Property> getPropertyByLocation(Location location);

    Set<Property> getAllProperties();

}
