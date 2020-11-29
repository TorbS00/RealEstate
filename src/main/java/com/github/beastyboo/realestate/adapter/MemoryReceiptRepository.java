package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.port.ReceiptRepository;
import org.bukkit.entity.Player;

import java.time.LocalDate;
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
        return false;
    }

    @Override
    public boolean viewAllReceiptsGUI(Player player) {
        return false;
    }

    @Override
    public Optional<Receipt> getReceipt(UUID uuid) {
        return Optional.ofNullable(receiptMemory.get(uuid));
    }

    @Override
    public Set<Receipt> getAllReceipts() {
        return new HashSet<>(receiptMemory.values());
    }
}
