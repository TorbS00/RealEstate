package com.github.beastyboo.realestate.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.github.beastyboo.realestate.entry.RealEstateAPI;
import org.bukkit.entity.Player;

/**
 * Created by Torbie on 01.12.2020.
 */
@CommandAlias("receipt|rec")
@Description("The core command for all managing properties")
public class ReceiptCmd extends BaseCommand{

    @HelpCommand
    @Private
    public void cmdHelp(CommandHelp help) {
        help.showHelp();
    }


    @Subcommand("me")
    @Description("Display all your receipts")
    @CommandPermission("receipt.me")
    public void cmdMe(Player player) {
        RealEstateAPI.getINSTANCE().viewYourReceiptsGUI(player);
    }

    @Subcommand("gui|list")
    @Description("Display all receipts for sale.")
    @CommandPermission("receipt.list")
    public void cmdList(Player player) {
        RealEstateAPI.getINSTANCE().viewAllReceiptsGUI(player);
    }

    @Subcommand("view")
    @Description("View someone's receipts")
    @CommandPermission("receipt.view")
    @CommandCompletion("@players")
    public void cmdViewTarget(Player player,@Name("player_name") OnlinePlayer target) {
        RealEstateAPI.getINSTANCE().viewTargetReceiptsGUI(player, target.getPlayer());
    }

}
