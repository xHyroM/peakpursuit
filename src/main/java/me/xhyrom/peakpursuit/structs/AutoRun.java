package me.xhyrom.peakpursuit.structs;

import me.xhyrom.peakpursuit.PeakPursuit;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class AutoRun {
    public int interval;
    public int minPlayers;

    public AutoRun(int interval, int maxPlayers) {
        this.interval = interval;
        this.minPlayers = maxPlayers;
    }

    public void start(Koth koth) {
        Bukkit.getScheduler().runTaskTimer(PeakPursuit.getInstance(), () -> {
            if (Bukkit.getOnlinePlayers().size() >= minPlayers && !koth.isRunning) koth.start();
        }, 0, interval * 20L);
    }

    public static AutoRun fromConfig(HashMap<String, Object> map) {
        return new AutoRun(
                (int) map.get("interval"),
                (int) map.get("min-players")
        );
    }
}
