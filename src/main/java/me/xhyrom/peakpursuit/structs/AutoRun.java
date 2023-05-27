package me.xhyrom.peakpursuit.structs;

import java.util.HashMap;

public class AutoRun {
    public AutoRunEvery every;
    public AutoRunVotes votes;

    public AutoRun(AutoRunEvery every, AutoRunVotes votes) {
        this.every = every;
        this.votes = votes;
    }

    public void start(Koth koth) {
        if (every.enabled) every.start(koth);
    }

    public static AutoRun fromConfig(HashMap<String, Object> map) {
        return new AutoRun(
                AutoRunEvery.fromConfig((HashMap<String, Object>) map.get("every")),
                AutoRunVotes.fromConfig((HashMap<String, Object>) map.get("votes"))
        );
    }
}
