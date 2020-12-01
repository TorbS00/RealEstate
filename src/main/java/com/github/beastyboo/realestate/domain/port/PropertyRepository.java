package com.github.beastyboo.realestate.domain.port;

import com.github.beastyboo.realestate.domain.entity.Property;
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

    boolean createProperty(String name, Player player, Location location, double price);

    boolean deleteProperty(Player player, Location location);

    boolean buyProperty(Player player, Location location);

    boolean changePropertyPrice(Player player, Location location, double price);

    boolean viewAllPropertiesGUI(Player player);

    boolean viewTargetPropertiesGUI(Player player, Player target);

    Optional<Property> getPropertyByID(UUID uuid);

    Optional<Property> getPropertyByLocation(Location location);

    Set<Property> getAllProperties();

}
