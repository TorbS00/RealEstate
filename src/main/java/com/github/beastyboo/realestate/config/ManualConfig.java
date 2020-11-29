package com.github.beastyboo.realestate.config;

import com.github.beastyboo.realestate.adapter.MemoryPropertyPlayer;
import com.github.beastyboo.realestate.adapter.MemoryPropertyRepository;
import com.github.beastyboo.realestate.adapter.MemoryReceiptRepository;
import com.github.beastyboo.realestate.application.RealEstate;
import com.github.beastyboo.realestate.domain.port.PropertyPlayerRepository;
import com.github.beastyboo.realestate.domain.port.PropertyRepository;
import com.github.beastyboo.realestate.domain.port.ReceiptRepository;

/**
 * Created by Torbie on 29.11.2020.
 */
public class ManualConfig {

    private final RealEstate core;
    private final PropertyRepository propertyRepository;
    private final ReceiptRepository receiptRepository;
    private final PropertyPlayerRepository propertyPlayerRepository;

    public ManualConfig(RealEstate core) {
        this.core = core;
        propertyRepository = new MemoryPropertyRepository(core);
        receiptRepository = new MemoryReceiptRepository(core);
        propertyPlayerRepository = new MemoryPropertyPlayer(core);
    }




}
