package me.theboykiss.ovh.whitelistaccount;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        String playerIp = event.getAddress().getHostAddress();

        FileConfiguration config = WhitelistAccount.getInstance().getPlayersConfig();
        List<String> whitelist = config.getStringList("whitelist");

        if (!whitelist.contains(playerName)) {
            return;
        }

        String whitelistedIp = config.getString("players." + playerName + ".ip");

        String noIpAssignedMessage = WhitelistAccount.getInstance().getMessagesConfig().getString("no_ip_assigned");
        String ipMismatchMessage = WhitelistAccount.getInstance().getMessagesConfig().getString("ip_mismatch");

        if (whitelistedIp == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', noIpAssignedMessage));
        } else if (!whitelistedIp.equals(playerIp)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', ipMismatchMessage));
            try {
                String discordId = config.getString("players." + playerName + ".discord_id");
                DiscordBot.getInstance().sendVerificationMessage(playerName, playerIp, discordId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
