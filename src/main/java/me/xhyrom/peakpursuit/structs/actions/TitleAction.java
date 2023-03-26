package me.xhyrom.peakpursuit.structs.actions;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

public class TitleAction implements Action {
    private String title;
    private String subtitle = "";

    public TitleAction(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public void execute(Player player, int place) {
        player.showTitle(Title.title(
                MiniMessage.miniMessage().deserialize(
                        title,
                        Placeholder.parsed("player", player.getName()),
                        Placeholder.parsed("place", String.valueOf(place))
                ),
                MiniMessage.miniMessage().deserialize(
                        subtitle,
                        Placeholder.parsed("player", player.getName()),
                        Placeholder.parsed("place", String.valueOf(place))
                ))
        );
    }
}