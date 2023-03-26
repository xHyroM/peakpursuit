package me.xhyrom.peakpursuit.structs.actions;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageAction implements Action {
    private String message;
    private boolean broadcast;

    public MessageAction(String message, boolean broadcast) {
        this.message = message;
        this.broadcast = broadcast;
    }

    @Override
    public void execute(Player player, int place) {
        if (broadcast) {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                    this.message,
                    Placeholder.parsed("player", player.getName()),
                    Placeholder.parsed("place", String.valueOf(place))
            ));

            return;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                this.message,
                Placeholder.parsed("player", player.getName()),
                Placeholder.parsed("place", String.valueOf(place))
        ));
    }
}