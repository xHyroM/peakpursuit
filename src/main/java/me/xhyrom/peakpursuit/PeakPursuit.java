package me.xhyrom.peakpursuit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIConfig;
import lombok.Getter;
import me.xhyrom.peakpursuit.commands.PeakPursuitCommand;
import me.xhyrom.peakpursuit.hooks.Hooks;
import me.xhyrom.peakpursuit.listeners.PlayerListener;
import me.xhyrom.peakpursuit.storage.Storage;
import me.xhyrom.peakpursuit.storage.structs.Votes;
import me.xhyrom.peakpursuit.structs.Koth;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class PeakPursuit extends JavaPlugin {
    @Getter
    private static PeakPursuit instance;
    @Getter
    private final HashMap<String, Koth> koths = new HashMap<>();
    public FileConfiguration config = getConfig();
    @Getter
    private Storage storage;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        CommandAPI.onEnable();

        storage = new Storage(
                this.getDataFolder().getPath(),
                "data.db",
                config.getString("storage.table")
        );
        storage.connection.buildEntitySchema(storage.table, Votes.class);

        config.getMapList("koth").forEach(map -> koths.put((String) map.get("name"), Koth.fromConfig(map)));

        Hooks.init();
        PeakPursuitCommand.register();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
