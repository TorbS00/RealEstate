package com.github.beastyboo.realestate.domain.holder;

import com.github.beastyboo.realestate.domain.entity.Receipt;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

/**
 * Created by Torbie on 29.11.2020.
 */
public class ReceiptInventoryHolder implements InventoryHolder {

    private final Map<Integer, Receipt> receiptBySlot;

    public ReceiptInventoryHolder(Map<Integer, Receipt> receiptBySlot) {
        this.receiptBySlot = receiptBySlot;
    }

    public Map<Integer, Receipt> getReceiptBySlot() {
        return receiptBySlot;
    }

    @Override
    public Inventory getInventory() {
        return this.getInventory();
    }
}
