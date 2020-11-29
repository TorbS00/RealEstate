package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.config.RealEstateAPI;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.holder.ReceiptInventoryHolder;
import com.github.beastyboo.realestate.domain.port.ReceiptRepository;
import com.github.beastyboo.realestate.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Torbie on 29.11.2020.
 */
public class MemoryReceiptRepository implements ReceiptRepository{

    private final RealEstate core;
    private final Map<UUID, Receipt> receiptMemory;

    public MemoryReceiptRepository(RealEstate core) {
        this.core = core;
        receiptMemory = new HashMap<>();
    }

    @Override
    public void load() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean createReceipt(UUID buyer, UUID seller, double price, LocalDate date) {
        RealEstateAPI.getINSTANCE().createPropertyPlayer(seller);
        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(seller);
        String formatDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.US));

        Receipt receipt = new Receipt.Builder(buyer, seller, price, formatDate).build();
        propertyPlayer.get().getReceipts().add(receipt);
        receiptMemory.put(receipt.getId(), receipt);
        return true;
    }

    @Override
    public boolean viewAllReceiptsGUI(Player player) {
        final Map<Integer, Receipt> receiptBySlot = new HashMap<>();
        final Inventory inventory = Bukkit.createInventory(new ReceiptInventoryHolder(receiptBySlot), 54, "§cAll Receipts");
        this.drawReceiptInventory(inventory, receiptBySlot);

        player.openInventory(inventory);
        return true;
    }

    @Override
    public Optional<Receipt> getReceipt(UUID uuid) {
        return Optional.ofNullable(receiptMemory.get(uuid));
    }

    @Override
    public Set<Receipt> getAllReceipts() {
        return new HashSet<>(receiptMemory.values());
    }

    private void drawReceiptInventory(Inventory inventory, Map<Integer, Receipt> receiptBySlot) {
        int i = 0;
        final List<String> lore = new ArrayList<>();

        for(Receipt receipt : receiptMemory.values()) {
            lore.clear();

            lore.add("Buyer: " + Bukkit.getOfflinePlayer(receipt.getBuyer()));
            lore.add("Seller: " + Bukkit.getOfflinePlayer(receipt.getSeller()));
            lore.add("ID: " + receipt.getId().toString());
            lore.add("Date: " + receipt.getDate());
            lore.add("Price: " + String.valueOf(receipt.getPrice()));

            inventory.setItem(i, InventoryUtil.INSTANCE.itemFactory(Material.PAPER, receipt.getDate(), lore));
            receiptBySlot.put(i, receipt);
            i++;
        }
    }

}
