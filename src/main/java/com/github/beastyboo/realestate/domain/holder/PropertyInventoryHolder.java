package com.github.beastyboo.realestate.domain.holder;

import com.github.beastyboo.realestate.domain.entity.Property;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

/**
 * Created by Torbie on 29.11.2020.
 */
public class PropertyInventoryHolder implements InventoryHolder{

    private final Map<Integer, Property> propertyBySlot;

    public PropertyInventoryHolder(Map<Integer, Property> propertyBySlot) {
        this.propertyBySlot = propertyBySlot;
    }

    @Override
    public Inventory getInventory() {
        return this.getInventory();
    }
}
