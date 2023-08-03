package me.theboykiss.ovh.whitelistaccount;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class DiscordBot {

    private static DiscordBot instance;
    private TextChannel channel;

    private DiscordBot() throws LoginException, InterruptedException, ExecutionException {
        FileConfiguration config = WhitelistAccount.getInstance().getConfig();
        String token = config.getString("discord.token");
        String channelId = config.getString("discord.channel_id");

        JDABuilder builder = JDABuilder.createDefault(token);
        JDA jda = builder.build().awaitReady();

        channel = jda.getTextChannelById(channelId);
    }

    public static DiscordBot getInstance() throws LoginException, InterruptedException, ExecutionException {
        if (instance == null) {
            instance = new DiscordBot();
        }
        return instance;
    }

    public void sendMessage(String message) {
        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Alerta de seguridad");
            embed.setDescription(message);
            embed.setColor(Color.RED);

            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    public void sendVerificationMessage(String playerName, String playerIp, String discordId) {
        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Verificación de IP");
            embed.setDescription("<@" + discordId + ">, ¿eres tú quien está intentando acceder a tu cuenta con la IP " + playerIp + "?");
            embed.setColor(Color.BLUE);

            channel.sendMessageEmbeds(embed.build()).queue(message -> {
                Emoji checkEmoji = Emoji.fromUnicode("✅");
                Emoji crossEmoji = Emoji.fromUnicode("❌");

                message.addReaction(checkEmoji).queue();
                message.addReaction(crossEmoji).queue();
                message.getJDA().addEventListener(new ReactionListener(message.getId(), playerName, playerIp));
            });
        }
    }



}