package me.xhyrom.peakpursuit.hooks;

import lombok.experimental.UtilityClass;
import me.xhyrom.peakpursuit.PeakPursuit;
import org.bukkit.Bukkit;

@UtilityClass
public class Hooks {
    public static PAPIExpansion papiExpansion;

    public static void init() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiExpansion = new PAPIExpansion(PeakPursuit.getInstance());
            papiExpansion.register();
        }
    }
}
