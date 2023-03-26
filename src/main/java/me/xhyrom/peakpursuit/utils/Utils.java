package me.xhyrom.peakpursuit.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<Player> getPlayersInRegion(String regionName, String worldName) {
        World world = BukkitAdapter.adapt(Bukkit.getWorld(worldName));

        ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(world)
                .getRegion(regionName);

        ArrayList<Player> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (region.contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())) {
                players.add(p);
            }
        }

        return players;
    }
}
