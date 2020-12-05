package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.adapter.typeadapter.PropertyPlayerTypeAdapter;
import com.github.beastyboo.realestate.adapter.typeadapter.PropertyTypeAdapter;
import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.entry.RealEstateAPI;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.holder.PropertyInventoryHolder;
import com.github.beastyboo.realestate.domain.port.PropertyRepository;
import com.github.beastyboo.realestate.util.MessageType;
import com.github.beastyboo.realestate.util.RealEstateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ryanhamshire.GriefPrevention.Claim;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Torbie on 29.11.2020.
 */
public class MemoryPropertyRepository implements PropertyRepository {

    private final RealEstate core;
    private final Map<UUID, Property> propertyMemory;
    private final Gson gson;
    private final File folder;

    public MemoryPropertyRepository(RealEstate core) {
        this.core = core;
        propertyMemory = new HashMap<>();
        gson = this.getGson();
        folder = new File(core.getPlugin().getDataFolder(), "properties");
    }

    @Override
    public void load() {
        if(!folder.exists()) {
            folder.mkdirs();
        }
        File[] directoryListing = folder.listFiles();
        if (directoryListing == null) {
            return;
        }
        for (File child : directoryListing) {
            String json = RealEstateUtil.INSTANCE.loadContent(child);
            Property property = this.deserialize(json);
            propertyMemory.put(property.getId(), property);
        }
    }

    @Override
    public void close() {
        for(Property property : propertyMemory.values()) {
            File file = new File(folder, property.getId().toString() + ".json");
            if(!folder.exists()) {
                folder.mkdirs();
            }
            String json = this.serialize(property);
            RealEstateUtil.INSTANCE.saveFile(file, json);
        }
    }

    @Override
    public boolean createProperty(String name, Player player, Location location, double price) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(property.isPresent()) {
            core.sendMessage(player, MessageType.PROPERTY_ALREADY_EXIST);
            return false;
        }

        Claim claim = core.getGriefPrevention().getClaimAt(location, false, null);

        if(claim.ownerID != player.getUniqueId()) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_CLAIM_OWNER);
            return false;
        }

        RealEstateAPI.getINSTANCE().createPropertyPlayer(player.getUniqueId());
        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(player.getUniqueId());

        Property newProperty = new Property.Builder(name, claim.ownerID, claim, price, player.getLocation()).build();
        propertyPlayer.get().getProperties().add(newProperty);
        propertyMemory.put(newProperty.getId(), newProperty);

        core.sendMessage(player, MessageType.PROPERTY_CREATED);
        return true;
    }

    @Override
    public boolean deleteProperty(Player player, Location location) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(!property.isPresent()) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_FOUND);
            return false;
        }

        if(player.getUniqueId() != property.get().getSeller() || !player.isOp()) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_CLAIM_OWNER);
            return false;
        }

        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(property.get().getSeller());
        propertyPlayer.get().getProperties().remove(property.get());
        propertyMemory.remove(property.get().getId(), property.get());

        core.sendMessage(player, MessageType.PROPERTY_DELETE);
        return true;
    }

    @Override
    public boolean buyProperty(Player player, Location location) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(!property.isPresent()) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_FOUND);
            return false;
        }

        if(player.getUniqueId() == property.get().getSeller()) {
            core.sendMessage(player, MessageType.PROPERTY_SELLER_IS_BUYER);
            return false;
        }

        double price = property.get().getPrice();

        Economy econ = core.getEconomy();

        if(econ.getBalance(player) < price) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_ENOUGH_MONEY);
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

        core.sendMessage(player, MessageType.PROPERTY_BOUGHT);
        return true;
    }

    @Override
    public boolean changePropertyPrice(Player player, Location location, double price) {
        Optional<Property> property = this.getPropertyByLocation(location);

        if(!property.isPresent()) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_FOUND);
            return false;
        }

        if(player.getUniqueId() != property.get().getSeller() || !player.isOp()) {
            core.sendMessage(player, MessageType.PROPERTY_NOT_CLAIM_OWNER);
            return false;
        }

        property.get().setPrice(price);
        core.sendMessage(player, MessageType.PROPERTY_PRICE_CHANGED);
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

    @Override
    public boolean viewTargetPropertiesGUI(Player player, Player target) {

        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(target.getUniqueId());

        if(!propertyPlayer.isPresent()) {
            return false;
        }

        final Map<Integer, Property> propertyBySlot = new HashMap<>();
        final Inventory inventory = Bukkit.createInventory(new PropertyInventoryHolder(propertyBySlot), 54, "§c" + target.getName() + "'s Properties");
        this.drawTargetPropertyInventory(target, inventory, propertyBySlot);
        player.openInventory(inventory);

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

    private void drawTargetPropertyInventory(Player target, Inventory inventory, Map<Integer, Property> propertyBySlot) {
        int i = 0;
        final List<String> lore = new ArrayList<>();

        for(Property property : RealEstateAPI.getINSTANCE().getYourProperties(target.getUniqueId())) {
            lore.clear();

            lore.add("Name: " + property.getName());
            lore.add("Seller: " + Bukkit.getOfflinePlayer(property.getSeller()).getName());
            lore.add("Price: " + String.valueOf(property.getPrice()));

            inventory.setItem(i, RealEstateUtil.INSTANCE.itemFactory(Material.CHEST, property.getName(), lore));
            propertyBySlot.put(i, property);
            i++;
        }
    }

    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Property.class, new PropertyTypeAdapter(core))
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    private String serialize(Property value) {
        return this.gson.toJson(value);
    }

    private Property deserialize(String json) {
        return this.gson.fromJson(json, Property.class);
    }

}
