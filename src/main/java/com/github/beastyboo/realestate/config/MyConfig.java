package com.github.beastyboo.realestate.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;

/**
 * Created by Torbie on 03.12.2020.
 */

@ConfHeader({"This plugin is created a managed by BeastCraft3/BeastyBoo", "Github for project: https://github.com/BeastyBoo/RealEstate \n"})
public interface MyConfig {

    @ConfDefault.DefaultBoolean(true)
    @ConfComments({"If this is true, a player will be trusted on property purchase automatically if its a sub-claim."})
    boolean trustPlayerOnPurchase();

    @ConfDefault.DefaultBoolean(true)
    @ConfComments({"This will transfer the claims blocks between buyer and seller if the property is not a sub-claim."})
    boolean transferClaimBlocksOnParentClaims();

    @ConfDefault.DefaultBoolean(true)
    @ConfComments({"If this is true, the server prefix will be displayed in front of every message from this plugin. "})
    boolean prefixChatOnCommands();

    @ConfComments({"The message shown when a certain thing happens"})
    @ConfDefault.DefaultString("&cMyServer")
    String serverPrefix();

}
