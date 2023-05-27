package me.xhyrom.peakpursuit.structs;

import me.xhyrom.peakpursuit.PeakPursuit;
import me.xhyrom.peakpursuit.storage.structs.Votes;

import java.util.HashMap;

public class AutoRunVotes {
    public boolean enabled;
    public int required;

    public AutoRunVotes(boolean enabled, int required) {
        this.enabled = enabled;
        this.required = required;
    }

    public void start(Koth koth, int count) {
        if (count < required) return;

        koth.start();

        PeakPursuit.getInstance().getStorage().connection
                .save(PeakPursuit.getInstance().getStorage().table, new Votes(0));
    }

    public static AutoRunVotes fromConfig(HashMap<String, Object> map) {
        return new AutoRunVotes(
                (boolean) map.get("enabled"),
                (int) map.get("required")
        );
    }
}
