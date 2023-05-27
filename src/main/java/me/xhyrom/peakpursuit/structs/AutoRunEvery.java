package me.xhyrom.peakpursuit.structs;

import me.xhyrom.peakpursuit.PeakPursuit;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class AutoRunEvery {
    public boolean enabled;
    public int interval;
    public int minPlayers;

    public AutoRunEvery(boolean enabled, int interval, int maxPlayers) {
        this.enabled = enabled;
        this.interval = interval;
        this.minPlayers = maxPlayers;
    }

    public void start(Koth koth) {
        Bukkit.getScheduler().runTaskTimer(PeakPursuit.getInstance(), () -> {
            if (Bukkit.getOnlinePlayers().size() >= minPlayers && !koth.isRunning) koth.start();
        }, 0, interval * 20L);
    }

    public static AutoRunEvery fromConfig(HashMap<String, Object> map) {
        return new AutoRunEvery(
                (boolean) map.get("enabled"),
                (int) map.get("interval"),
                (int) map.get("min-players")
        );
    }
}
