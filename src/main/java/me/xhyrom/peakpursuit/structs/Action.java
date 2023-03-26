package me.xhyrom.peakpursuit.structs;

import org.bukkit.entity.Player;

public interface Action {
    void execute(Player player, int place);
}
