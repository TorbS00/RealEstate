package com.github.beastyboo.realestate.application;

import com.github.beastyboo.realestate.config.ManualConfig;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Torbie on 29.11.2020.
 */
public class RealEstate {

    private final JavaPlugin plugin;
    private final ManualConfig config;
    private Economy economy;

    public RealEstate(JavaPlugin plugin) {
        this.plugin = plugin;
        config = new ManualConfig(this);
        economy = null;
    }

    void load() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loading up " + plugin.getName());

        if (!setupEconomy() ) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + plugin.getName() + " - Disabled due to no Vault dependency found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        if(!plugin.getServer().getPluginManager().getPlugin("GriefPrevention").isEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + plugin.getName() + " - Disabled due to GriefPrevention not found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

    }

    void close() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Closing down " + plugin.getName());
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public DataStore getGriefPrevention() {
        return GriefPrevention.instance.dataStore;
    }

    public ManualConfig getConfig() {
        return config;
    }

    public Economy getEconomy() {
        return economy;
    }

}
