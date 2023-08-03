package me.theboykiss.ovh.whitelistaccount;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ExecutionException;

public class WhitelistAccountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Uso: /whitelistaccount <nick> <ip> [discord_id]");
            return false;
        }

        String playerName = args[0];
        String newIp = args[1];
        String discordId = null;

        FileConfiguration config = WhitelistAccount.getInstance().getPlayersConfig();
        if (args.length == 3) {
            discordId = args[2];
        } else {
            discordId = config.getString("players." + playerName + ".discord_id");
        }

        config.set("players." + playerName + ".ip", newIp);
        if (discordId != null) {
            config.set("players." + playerName + ".discord_id", discordId);
        }
        WhitelistAccount.getInstance().saveConfig();

        sender.sendMessage("La dirección IP de " + playerName + " ha sido actualizada.");
        if (discordId != null) {
            sender.sendMessage("La ID de Discord de " + playerName + " ha sido actualizada a " + discordId);
        }
        return true;
    }
}
