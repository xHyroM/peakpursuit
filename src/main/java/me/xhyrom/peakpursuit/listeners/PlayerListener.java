package me.xhyrom.peakpursuit.listeners;

import me.xhyrom.peakpursuit.PeakPursuit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PeakPursuit.getInstance().getKoths().values().stream()
                .filter(koth -> koth.isRunning)
                .forEach(koth -> event.getPlayer().showBossBar(koth.getBossBar().bossBar));
    }
}
