package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.config.RealEstateAPI;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.holder.PropertyInventoryHolder;
import com.github.beastyboo.realestate.domain.port.PropertyRepository;
import com.github.beastyboo.realestate.util.RealEstateUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.LocalDate;
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
    public boolean createProperty(String name, Player player, Location location, double price) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(property.isPresent()) {
            return false;
        }

        Claim claim = core.getGriefPrevention().getClaimAt(location, false, null);

        if(claim.ownerID != player.getUniqueId()) {
            return false;
        }

        RealEstateAPI.getINSTANCE().createPropertyPlayer(player.getUniqueId());
        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(player.getUniqueId());

        Property newProperty = new Property.Builder(name, claim.ownerID, claim, price, player.getLocation()).build();
        propertyPlayer.get().getProperties().add(newProperty);
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

        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(property.get().getSeller());
        propertyPlayer.get().getProperties().remove(property.get());
        propertyMemory.remove(property.get().getId(), property.get());
        return true;
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

        LocalDate date = LocalDate.now();
        RealEstateAPI.getINSTANCE().createReceipt(player.getUniqueId(), property.get().getSeller(), price, date);
        Optional<PropertyPlayer> propertySeller = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(property.get().getSeller());
        propertySeller.get().getProperties().remove(property.get());

        propertyMemory.remove(property.get().getId(), property.get());
        return true;
    }

    @Override
    public boolean changePropertyPrice(Player player, Location location, double price) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(!property.isPresent()) {
            return false;
        }

        if(player.getUniqueId() != property.get().getSeller() || !player.isOp()) {
            return false;
        }

        property.get().setPrice(price);
        return true;
    }

    @Override
    public boolean viewAllPropertiesGUI(Player player) {

        final Map<Integer, Property> propertyBySlot = new HashMap<>();
        final Inventory inventory = Bukkit.createInventory(new PropertyInventoryHolder(propertyBySlot), 54, "§cAll Properties");
        this.drawPropertyInventory(inventory, propertyBySlot);

        player.openInventory(inventory);

        return true;
    }

    private void drawPropertyInventory(Inventory inventory, Map<Integer, Property> propertyBySlot) {
        int i = 0;
        final List<String> lore = new ArrayList<>();

        for(Property property : propertyMemory.values()) {
            lore.clear();

            lore.add("Name: " + property.getName());
            lore.add("Seller: " + Bukkit.getOfflinePlayer(property.getSeller()).getName());
            lore.add("Price: " + String.valueOf(property.getPrice()));

            inventory.setItem(i, RealEstateUtil.INSTANCE.itemFactory(Material.CHEST, property.getName(), lore));
            propertyBySlot.put(i, property);
            i++;
        }
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
