package com.github.beastyboo.realestate.application;

import co.aikar.commands.PaperCommandManager;
import com.github.beastyboo.realestate.command.PropertyCmd;
import com.github.beastyboo.realestate.command.ReceiptCmd;
import com.github.beastyboo.realestate.config.MyConfig;
import com.github.beastyboo.realestate.config.MyConfigManager;
import com.github.beastyboo.realestate.domain.port.MessagePort;
import com.github.beastyboo.realestate.entry.RealEstateAPI;
import com.github.beastyboo.realestate.util.MessageType;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Torbie on 29.11.2020.
 */
public class RealEstate {

    private final JavaPlugin plugin;
    private final RealEstateAPI api;
    private final PaperCommandManager manager;
    private final MyConfigManager<MyConfig> configManager;
    private final MyConfigManager<MessagePort> messageManager;
    private final MyConfig config;
    private final MessagePort message;
    private Economy economy;

    public RealEstate(JavaPlugin plugin) {
        this.plugin = plugin;
        api = new RealEstateAPI(this);
        manager = new PaperCommandManager(plugin);
        configManager = MyConfigManager.create(plugin.getDataFolder().toPath(), "config.yml", MyConfig.class);
        messageManager = MyConfigManager.create(plugin.getDataFolder().toPath(), "message.yml", MessagePort.class);
        configManager.reloadConfig();
        messageManager.reloadConfig();
        config = configManager.getConfigData();
        message = messageManager.getConfigData();
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
        this.registerCommands(manager);

        RealEstateAPI.getINSTANCE().load();
    }

    void close() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Closing down " + plugin.getName());
        RealEstateAPI.getINSTANCE().close();
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

    private void registerCommands(PaperCommandManager manager) {
        manager.enableUnstableAPI("help");

        //manager.getCommandCompletions().registerAsyncCompletion("property", c -> Arrays.asList("create", "buy", "delete", "gui", "me", "change price", "view"));

        manager.registerCommand(new PropertyCmd());
        manager.registerCommand(new ReceiptCmd());

        manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t) -> {
            plugin.getLogger().warning("Error occured while executing command: " + command.getName());
            return false;
        });
    }

    public void sendMessage(Player player, MessageType type) {
        if(config.prefixChatOnCommands() == true) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.serverPrefix() + " " +  message.messages().get(type)));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.messages().get(type)));
        }
    }

    public MyConfig getConfig() {
        return config;
    }

    public DataStore getGriefPrevention() {
        return GriefPrevention.instance.dataStore;
    }

    public Economy getEconomy() {
        return economy;
    }

}
