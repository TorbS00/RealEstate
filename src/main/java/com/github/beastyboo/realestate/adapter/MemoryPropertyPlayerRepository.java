package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.holder.PropertyInventoryHolder;
import com.github.beastyboo.realestate.domain.holder.ReceiptInventoryHolder;
import com.github.beastyboo.realestate.domain.port.PropertyPlayerRepository;
import com.github.beastyboo.realestate.util.RealEstateUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * Created by Torbie on 29.11.2020.
 */
public class MemoryPropertyPlayerRepository implements PropertyPlayerRepository{

    private final RealEstate core;
    private final Map<UUID, PropertyPlayer> memoryPropertyPlayer;

    public MemoryPropertyPlayerRepository(RealEstate core) {
        this.core = core;
        this.memoryPropertyPlayer = new HashMap<>();
    }

    @Override
    public void load() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean createPropertyPlayer(UUID uuid) {
        Optional<PropertyPlayer> propertyPlayer = this.getPropertyPlayerByID(uuid);

        if(propertyPlayer.isPresent()) {
            return false;
        }

        Set<Property> properties = new HashSet<>();
        Set<Receipt> receipts = new HashSet<>();

        PropertyPlayer newPropertyPlayer = new PropertyPlayer(uuid, properties, receipts);
        memoryPropertyPlayer.put(uuid, newPropertyPlayer);

        return true;
    }

    @Override
    public boolean viewYourPropertiesGUI(Player player) {
        Optional<PropertyPlayer> propertyPlayer = this.getPropertyPlayerByID(player.getUniqueId());

        if(!propertyPlayer.isPresent()) {
            return false;
        }

        final Map<Integer, Property> propertyBySlot = new HashMap<>();
        final Inventory inventory = Bukkit.createInventory(new PropertyInventoryHolder(propertyBySlot), 54, "§cProperty Profile for " + player.getName());
        this.drawPropertyInventory(propertyPlayer.get(), inventory, propertyBySlot);

        player.openInventory(inventory);
        return true;
    }

    @Override
    public boolean viewYourReceiptsGUI(Player player) {
        Optional<PropertyPlayer> propertyPlayer = this.getPropertyPlayerByID(player.getUniqueId());

        if(!propertyPlayer.isPresent()) {
            return false;
        }

        final Map<Integer, Receipt> receiptBySlot = new HashMap<>();
        final Inventory inventory = Bukkit.createInventory(new ReceiptInventoryHolder(receiptBySlot), 54, "§cReceipt Profile for " + player.getName());
        this.drawReceiptInventory(propertyPlayer.get(), inventory, receiptBySlot);

        player.openInventory(inventory);
        return true;
    }

    @Override
    public Optional<PropertyPlayer> getPropertyPlayerByID(UUID uuid) {
        return Optional.ofNullable(memoryPropertyPlayer.get(uuid));
    }

    @Override
    public Set<PropertyPlayer> getAllPropertyPlayers() {
        return new HashSet<>(memoryPropertyPlayer.values());
    }

    @Override
    public Set<Property> getYourProperties(UUID uuid) {
        return new HashSet<>(memoryPropertyPlayer.get(uuid).getProperties());
    }

    @Override
    public Set<Receipt> getYourReceipts(UUID uuid) {
        return new HashSet<>(memoryPropertyPlayer.get(uuid).getReceipts());
    }

    private void drawReceiptInventory(PropertyPlayer player, Inventory inventory, Map<Integer, Receipt> receiptBySlot) {
        int i = 0;
        final List<String> lore = new ArrayList<>();

        for(Receipt receipt : player.getReceipts()) {
            lore.clear();

            lore.add("Buyer: " + Bukkit.getOfflinePlayer(receipt.getBuyer()));
            lore.add("Seller: " + Bukkit.getOfflinePlayer(receipt.getSeller()));
            lore.add("ID: " + receipt.getId().toString());
            lore.add("Date: " + receipt.getDate());
            lore.add("Price: " + String.valueOf(receipt.getPrice()));

            inventory.setItem(i, RealEstateUtil.INSTANCE.itemFactory(Material.PAPER, receipt.getDate(), lore));
            receiptBySlot.put(i, receipt);
            i++;
        }
    }

    private void drawPropertyInventory(PropertyPlayer player, Inventory inventory, Map<Integer, Property> propertyBySlot) {
        int i = 0;
        final List<String> lore = new ArrayList<>();

        for(Property property : player.getProperties()) {
            lore.clear();

            lore.add("Name: " + property.getName());
            lore.add("Seller: " + Bukkit.getOfflinePlayer(property.getSeller()).getName());
            lore.add("Price: " + String.valueOf(property.getPrice()));

            inventory.setItem(i, RealEstateUtil.INSTANCE.itemFactory(Material.CHEST, property.getName(), lore));
            propertyBySlot.put(i, property);
            i++;
        }
    }

}
