package me.theboykiss.ovh.whitelistaccount;

import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;

public class ReactionListener extends ListenerAdapter {
    private final String messageId;
    private final String playerName;
    private final String playerIp;

    public ReactionListener(String messageId, String playerName, String playerIp) {
        this.messageId = messageId;
        this.playerName = playerName;
        this.playerIp = playerIp;
    }

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getMessageId().equals(messageId) && !event.getUser().isBot()) {
            EmojiUnion emojiUnion = event.getEmoji();
            String checkMarkEmoji = "✅";
            String reactionEmoji = emojiUnion.getAsReactionCode();

            if (reactionEmoji.equals(checkMarkEmoji)) {
                FileConfiguration config = WhitelistAccount.getInstance().getPlayersConfig();
                config.set("players." + playerName + ".ip", playerIp);
                WhitelistAccount.getInstance().saveConfig();
            }
            event.getChannel().retrieveMessageById(messageId).queue(message -> message.delete().queue());
        }
    }

}
