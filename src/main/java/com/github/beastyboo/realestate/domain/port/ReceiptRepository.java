package com.github.beastyboo.realestate.domain.port;

import com.github.beastyboo.realestate.domain.entity.Receipt;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public interface ReceiptRepository {

    void load();

    void close();

    boolean createReceipt(UUID buyer, UUID seller, double price, LocalDate date);

    boolean viewAllReceiptsGUI(Player player);

    boolean viewTargetReceiptsGUI(Player player, Player target);

    Optional<Receipt> getReceipt(UUID uuid);

    Set<Receipt> getAllReceipts();

}
