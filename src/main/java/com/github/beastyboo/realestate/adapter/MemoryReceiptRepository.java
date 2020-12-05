package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.adapter.typeadapter.PropertyPlayerTypeAdapter;
import com.github.beastyboo.realestate.adapter.typeadapter.ReceiptTypeAdapter;
import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.entry.RealEstateAPI;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.holder.ReceiptInventoryHolder;
import com.github.beastyboo.realestate.domain.port.ReceiptRepository;
import com.github.beastyboo.realestate.util.RealEstateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Torbie on 29.11.2020.
 */
public class MemoryReceiptRepository implements ReceiptRepository{

    private final RealEstate core;
    private final Map<UUID, Receipt> receiptMemory;
    private final Gson gson;
    private final File folder;

    public MemoryReceiptRepository(RealEstate core) {
        this.core = core;
        receiptMemory = new HashMap<>();
        gson = this.getGson();
        folder = new File(core.getPlugin().getDataFolder(), "receipts");
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
            Receipt receipt = this.deserialize(json);
            receiptMemory.put(receipt.getId(), receipt);
        }
    }

    @Override
    public void close() {
        for(Receipt receipt : receiptMemory.values()) {
            File file = new File(folder, receipt.getId().toString() + ".json");
            if(!folder.exists()) {
                folder.mkdirs();
            }
            String json = this.serialize(receipt);
            RealEstateUtil.INSTANCE.saveFile(file, json);
        }
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
    public boolean viewTargetReceiptsGUI(Player player, Player target) {

        Optional<PropertyPlayer> propertyPlayer = RealEstateAPI.getINSTANCE().getPropertyPlayerByID(target.getUniqueId());

        if(!propertyPlayer.isPresent()) {
            return false;
        }

        final Map<Integer, Receipt> receiptBySlot = new HashMap<>();
        final Inventory inventory = Bukkit.createInventory(new ReceiptInventoryHolder(receiptBySlot), 54, "§c" + target.getName() + "'s Receipts");
        this.drawTargetReceiptInventory(target, inventory, receiptBySlot);

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

            inventory.setItem(i, RealEstateUtil.INSTANCE.itemFactory(Material.PAPER, receipt.getDate(), lore));
            receiptBySlot.put(i, receipt);
            i++;
        }
    }

    private void drawTargetReceiptInventory(Player target, Inventory inventory, Map<Integer, Receipt> receiptBySlot) {
        int i = 0;
        final List<String> lore = new ArrayList<>();

        for(Receipt receipt : RealEstateAPI.getINSTANCE().getYourReceipts(target.getUniqueId())) {
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

    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Receipt.class, new ReceiptTypeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    private String serialize(Receipt value) {
        return this.gson.toJson(value);
    }

    private Receipt deserialize(String json) {
        return this.gson.fromJson(json, Receipt.class);
    }

}
