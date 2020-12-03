package com.github.beastyboo.realestate.domain.port;

import com.github.beastyboo.realestate.util.MessageType;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;

import java.util.Map;

/**
 * Created by Torbie on 03.12.2020.
 */

@ConfHeader({"This plugin is created a managed by BeastCraft3/BeastyBoo", "Github for project: https://github.com/BeastyBoo/RealEstate \n"})
public interface MessagePort {

    @ConfDefault.DefaultMap({"PROPERTY_NOT_FOUND","&cProperty is not found!",
            "PROPERTY_CREATED","&cProperty created!",
            "PROPERTY_ALREADY_EXIST","&cProperty already exist",
            "PROPERTY_DELETE","&cProperty deleted!",
            "PROPERTY_BOUGHT","&cProperty bought, receipt created.",
            "PROPERTY_PRICE_CHANGED","&cProperty price changed",
            "PROPERTY_NOT_CLAIM_OWNER","&cYou're not the owner of this claim.",
            "PROPERTY_SELLER_IS_BUYER", "&cYou can't buy your own properties...",
            "PROPERTY_NOT_ENOUGH_MONEY", "&cYou dont have enough money for this property."
    })
    Map<MessageType, String> messages();

}
