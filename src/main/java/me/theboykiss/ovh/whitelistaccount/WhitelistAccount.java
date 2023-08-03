package me.theboykiss.ovh.whitelistaccount;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class WhitelistAccount extends JavaPlugin {

    private static WhitelistAccount instance;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("whitelistaccount").setExecutor(new WhitelistAccountCommand());
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
        loadMessagesConfig();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static WhitelistAccount getInstance() {
        return instance;
    }

    public FileConfiguration getPlayersConfig() {
        return this.getConfig();
    }

    public void loadMessagesConfig() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
}
