package me.xhyrom.peakpursuit.structs.actions;

import org.bukkit.entity.Player;

public class SoundAction implements Action {
    private String sound;
    private float volume = 1;
    private float pitch = 1;

    public SoundAction(String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(Player player, int place) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
