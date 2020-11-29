package com.github.beastyboo.realestate.domain.port;

import com.github.beastyboo.realestate.domain.entity.Property;
import com.github.beastyboo.realestate.domain.entity.PropertyPlayer;
import com.github.beastyboo.realestate.domain.entity.Receipt;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public interface PropertyPlayerRepository {

    void load();

    void close();

    boolean createPropertyPlayer(UUID uuid);

    Optional<PropertyPlayer> getPropertyPlayerByID(UUID uuid);

    Set<PropertyPlayer> getAllPropertyPlayers();

    Set<Property> getYourProperties(UUID uuid);

    Set<Receipt> getYourReceipts(UUID uuid);

}
