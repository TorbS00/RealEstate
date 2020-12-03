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
@CommandAlias("property|prop")
@Description("The core command for all managing properties")
public class PropertyCmd extends BaseCommand{


    @HelpCommand
    @Private
    public void cmdHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @Description("Creates a property")
    @CommandPermission("property.create")
    public void cmdCreate(Player player,@Name("name") String name ,@Name("price") double price) {
        RealEstateAPI.getINSTANCE().createProperty(name, player, player.getLocation(), price);
    }

    @Subcommand("buy")
    @Description("Buy a property")
    @CommandPermission("property.buy")
    public void cmdBuy(Player player) {
        RealEstateAPI.getINSTANCE().buyProperty(player, player.getLocation());
    }

    @Subcommand("delete")
    @Description("Delete's a property")
    @CommandPermission("property.delete")
    public void cmdDelete(Player player) {
        RealEstateAPI.getINSTANCE().deleteProperty(player, player.getLocation());
    }

    @Subcommand("gui|list")
    @Description("Display all properties for sale.")
    @CommandPermission("property.list")
    public void cmdList(Player player) {
        RealEstateAPI.getINSTANCE().viewAllPropertiesGUI(player);
    }

    @Subcommand("me")
    @Description("Display all your properties")
    @CommandPermission("property.me")
    public void cmdMe(Player player) {
        RealEstateAPI.getINSTANCE().viewYourPropertiesGUI(player);
    }

    @Subcommand("change price")
    @Description("Change the property price")
    @CommandPermission("property.change.price")
    public void cmdChangePrice(Player player,@Name("price")  double price) {
        RealEstateAPI.getINSTANCE().changePropertyPrice(player, player.getLocation(), price);
    }

    @Subcommand("view")
    @Description("View someone's properties")
    @CommandPermission("property.view")
    @CommandCompletion("@players")
    public void cmdViewTarget(Player player, @Name("player_name") OnlinePlayer target) {
        RealEstateAPI.getINSTANCE().viewTargetPropertiesGUI(player, target.getPlayer());
    }
}
