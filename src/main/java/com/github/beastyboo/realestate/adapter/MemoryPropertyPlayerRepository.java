package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.port.PropertyPlayerRepository;
import org.bukkit.entity.Player;

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
        return false;
    }

    @Override
    public boolean viewYourReceiptsGUI(Player player) {
        return false;
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
}
