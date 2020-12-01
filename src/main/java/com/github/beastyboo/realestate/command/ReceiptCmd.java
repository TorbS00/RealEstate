package com.github.beastyboo.realestate.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.beastyboo.realestate.config.RealEstateAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Torbie on 01.12.2020.
 */
@CommandAlias("receipt|rec")
@Description("The core command for all managing properties")
public class ReceiptCmd extends BaseCommand{

    @HelpCommand
    public void cmdHelp(CommandSender sender, CommandHelp help) {
        sender.sendMessage("§cReceipt Help:");
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
    public void cmdViewTarget(Player player, Player target) {
        RealEstateAPI.getINSTANCE().viewTargetReceiptsGUI(player, target);
    }

}
