package me.xhyrom.peakpursuit.structs.actions;

import org.bukkit.entity.Player;

public interface Action {
    void execute(Player player, int place);
}
