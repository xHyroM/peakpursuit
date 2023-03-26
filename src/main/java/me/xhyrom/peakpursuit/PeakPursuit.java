package me.xhyrom.peakpursuit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import lombok.Getter;
import me.xhyrom.peakpursuit.commands.PeakPursuitCommand;
import me.xhyrom.peakpursuit.listeners.PlayerListener;
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
    private HashMap<String, Koth> koths = new HashMap<>();
    public FileConfiguration config = getConfig();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        CommandAPI.onEnable(this);

        config.getMapList("koth").forEach(map -> koths.put((String) map.get("name"), Koth.fromConfig(map)));

        PeakPursuitCommand.register();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
