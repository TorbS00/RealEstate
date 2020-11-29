package com.github.beastyboo.realestate.adapter;

import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.entity.Receipt;
import com.github.beastyboo.realestate.domain.port.ReceiptRepository;

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
    public Optional<Receipt> getReceipt(UUID uuid) {
        return Optional.ofNullable(receiptMemory.get(uuid));
    }

    @Override
    public Set<Receipt> getAllReceipts() {
        return new HashSet<>(receiptMemory.values());
    }
}
