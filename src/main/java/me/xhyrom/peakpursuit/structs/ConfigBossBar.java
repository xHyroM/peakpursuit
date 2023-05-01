package me.xhyrom.peakpursuit.structs;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfigBossBar {
    public final String defaultTitle;
    public Component title;
    public BossBar.Color color;
    public BossBar.Overlay overlay;
    public BossBar bossBar;

    public ConfigBossBar(String title, BossBar.Color color, BossBar.Overlay overlay) {
        this.defaultTitle = title;
        this.color = color;
        this.overlay = overlay;
    }

    public void create(Koth koth) {
        this.title = MiniMessage.miniMessage().deserialize(
                this.defaultTitle,
                Placeholder.parsed("name", koth.name)
        );
        this.bossBar = BossBar.bossBar(this.title, 1F, this.color, this.overlay);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showBossBar(this.bossBar);
        }
    }

    public void destroy() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hideBossBar(this.bossBar);
        }

        this.bossBar = null;
    }

    public static ConfigBossBar fromConfig(Map<?, ?> map) {
        return new ConfigBossBar(
                (String) map.get("title"),
                BossBar.Color.valueOf(((String) map.get("color")).toUpperCase()),
                BossBar.Overlay.valueOf(((String) map.get("overlay")).toUpperCase())
        );
    }
}
