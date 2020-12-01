package com.github.beastyboo.realestate.config;

import com.github.beastyboo.realestate.adapter.MemoryPropertyPlayerRepository;
import com.github.beastyboo.realestate.adapter.MemoryPropertyRepository;
import com.github.beastyboo.realestate.adapter.MemoryReceiptRepository;
import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.port.PropertyPlayerRepository;
import com.github.beastyboo.realestate.domain.port.PropertyRepository;
import com.github.beastyboo.realestate.domain.port.ReceiptRepository;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public class RealEstateAPI {

    private static RealEstateAPI INSTANCE;

    private final RealEstate core;
    private final PropertyRepository propertyRepository;
    private final ReceiptRepository receiptRepository;
    private final PropertyPlayerRepository propertyPlayerRepository;

    public RealEstateAPI(RealEstate core) {
        this.core = core;
        INSTANCE = this;
        propertyRepository = new MemoryPropertyRepository(core);
        receiptRepository = new MemoryReceiptRepository(core);
        propertyPlayerRepository = new MemoryPropertyPlayerRepository(core);
    }

    public void load() {
        propertyRepository.load();
        receiptRepository.load();
        propertyPlayerRepository.load();
    }

    public void close() {
        propertyPlayerRepository.close();
        receiptRepository.close();
        propertyRepository.close();
    }

    public boolean createPropertyPlayer(UUID uuid) {
        return propertyPlayerRepository.createPropertyPlayer(uuid);
    }

    public boolean viewYourPropertiesGUI(Player player) {
        return propertyPlayerRepository.viewYourPropertiesGUI(player);
    }

    public boolean viewYourReceiptsGUI(Player player) {
        return propertyPlayerRepository.viewYourReceiptsGUI(player);
    }

    public Optional<PropertyPlayer> getPropertyPlayerByID(UUID uuid) {
        return propertyPlayerRepository.getPropertyPlayerByID(uuid);
    }

    public Set<PropertyPlayer> getAllPropertyPlayers() {
        return propertyPlayerRepository.getAllPropertyPlayers();
    }

    public Set<Property> getYourProperties(UUID uuid) {
        return propertyPlayerRepository.getYourProperties(uuid);
    }

    public Set<Receipt> getYourReceipts(UUID uuid) {
        return propertyPlayerRepository.getYourReceipts(uuid);
    }

    public boolean createProperty(String name, Player player, Location location, double price) {
        return propertyRepository.createProperty(name, player, location, price);
    }

    public boolean deleteProperty(Player player, Location location) {
        return propertyRepository.deleteProperty(player, location);
    }

    public boolean buyProperty(Player player, Location location) {
        return propertyRepository.buyProperty(player, location);
    }

    public boolean changePropertyPrice(Player player, Location location, double price) {
        return propertyRepository.changePropertyPrice(player, location, price);
    }

    public boolean viewAllPropertiesGUI(Player player) {
        return propertyRepository.viewAllPropertiesGUI(player);
    }

    public boolean viewTargetPropertiesGUI(Player player, Player target) {
        return propertyRepository.viewTargetPropertiesGUI(player, target);
    }

    public Optional<Property> getPropertyByID(UUID uuid) {
        return propertyRepository.getPropertyByID(uuid);
    }

    public Optional<Property> getPropertyByLocation(Location location) {
        return propertyRepository.getPropertyByLocation(location);
    }

    public Set<Property> getAllProperties() {
        return propertyRepository.getAllProperties();
    }

    public boolean createReceipt(UUID buyer, UUID seller, double price, LocalDate date) {
        return receiptRepository.createReceipt(buyer, seller, price, date);
    }

    public boolean viewAllReceiptsGUI(Player player) {
        return receiptRepository.viewAllReceiptsGUI(player);
    }

    public boolean viewTargetReceiptsGUI(Player player, Player target) {
        return receiptRepository.viewTargetReceiptsGUI(player, target);
    }

    public Optional<Receipt> getReceipt(UUID uuid) {
        return receiptRepository.getReceipt(uuid);
    }

    public Set<Receipt> getAllReceipts() {
        return receiptRepository.getAllReceipts();
    }

    public static RealEstateAPI getINSTANCE() {
        return INSTANCE;
    }
}
