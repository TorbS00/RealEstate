package com.github.beastyboo.realestate.domain.port;

import com.github.beastyboo.realestate.domain.entity.Receipt;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Torbie on 29.11.2020.
 */
public interface ReceiptRepository {

    void load();

    void close();

    Optional<Receipt> getReceipt(UUID uuid);

    Set<Receipt> getAllReceipts();

}
