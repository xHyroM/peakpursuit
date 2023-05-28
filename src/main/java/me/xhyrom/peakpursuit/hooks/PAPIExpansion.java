package me.xhyrom.peakpursuit.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xhyrom.peakpursuit.PeakPursuit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIExpansion extends PlaceholderExpansion {
    private final PeakPursuit plugin;

    public PAPIExpansion(PeakPursuit plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "peakpursuit";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        switch (params) {
            case "votes":
                return plugin.getStorage().votes + "";
            default:
                return null;
        }
    }
}
