package me.xhyrom.peakpursuit.structs;

import lombok.Getter;
import me.xhyrom.peakpursuit.PeakPursuit;
import me.xhyrom.peakpursuit.structs.actions.*;
import me.xhyrom.peakpursuit.utils.Utils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Koth {
    public String name;
    private String worldName;
    private String regionName;
    @Getter
    private ConfigBossBar bossBar;
    private AutoRun autoRun;
    private int duration;
    private int startedAt;
    private HashMap<Integer, ArrayList<Action>> rewards;
    private ArrayList<UUID> players = new ArrayList<>();
    private HashMap<UUID, Integer> scores = new HashMap<>();
    public boolean isRunning = false;

    public Koth(String name, String worldName, String regionName, ConfigBossBar bossBar, AutoRun autoRun, int duration, HashMap<Integer, ArrayList<Action>> rewards) {
        this.name = name;
        this.worldName = worldName;
        this.regionName = regionName;
        this.bossBar = bossBar;
        this.autoRun = autoRun;
        this.duration = duration;
        this.rewards = rewards;

        this.autoRun.start(this);
    }

    public void start() {
        isRunning = true;
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                PeakPursuit.getInstance().config.getString("messages.start"),
                Placeholder.parsed("name", name)
        ));

        this.bossBar.create(this);
        this.startedAt = (int) (System.currentTimeMillis() / 1000L);

        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(PeakPursuit.getInstance(), () -> {
            for (Player player : Utils.getPlayersInRegion(this.regionName, this.worldName)) {
                scores.merge(player.getUniqueId(), 1, Integer::sum);
                if (!players.contains(player.getUniqueId())) {
                    players.add(player.getUniqueId());
                }

                Collections.sort(players, new ModuleComparator(scores));
                if (this.players.size() > 3)
                    this.players.remove(3);

                player.sendActionBar(MiniMessage.miniMessage().deserialize(
                        PeakPursuit.getInstance().config.getString("messages.action-bar"),
                        Placeholder.parsed("player", name),
                        Placeholder.parsed("score", String.valueOf(scores.get(player.getUniqueId())))
                ));
            }

            this.bossBar.bossBar.progress(1 - ((float) (System.currentTimeMillis() / 1000L - this.startedAt) / this.duration));
        }, 0, 20);

        Bukkit.getScheduler().runTaskLater(PeakPursuit.getInstance(), () -> {
            for (String message : PeakPursuit.getInstance().config.getStringList("messages.end")) {
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                        message,
                        Placeholder.parsed("name", name),
                        Placeholder.parsed("first", players.size() > 0 ? Bukkit.getOfflinePlayer(players.get(0)).getName() : "-"),
                        Placeholder.parsed("second", players.size() > 1 ? Bukkit.getOfflinePlayer(players.get(1)).getName() : "-"),
                        Placeholder.parsed("third", players.size() > 2 ? Bukkit.getOfflinePlayer(players.get(2)).getName() : "-"),
                        Placeholder.parsed("first_score", players.size() > 0 ? String.valueOf(scores.get(players.get(0))) : "-"),
                        Placeholder.parsed("second_score", players.size() > 1 ? String.valueOf(scores.get(players.get(1))) : "-"),
                        Placeholder.parsed("third_score", players.size() > 2 ? String.valueOf(scores.get(players.get(2))) : "-")
                ));
            }

            players.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                rewards.get(players.indexOf(uuid) + 1).forEach(action -> action.execute(player, players.indexOf(uuid) + 1));
            });

            players = new ArrayList<>();
            scores = new HashMap<>();

            this.bossBar.destroy();
            task.cancel();
            isRunning = false;
        }, duration * 20L);
    }

    public static Koth fromConfig(Map<?, ?> map) {
        HashMap<Integer, ArrayList<HashMap<String, Object>>> config_rewards = (HashMap<Integer, ArrayList<HashMap<String, Object>>>) map.get("rewards");
        HashMap<Integer, ArrayList<Action>> rewards = new HashMap<>();

        for (Map.Entry<Integer, ArrayList<HashMap<String, Object>>> entry : config_rewards.entrySet()) {
            ArrayList<Action> actions = new ArrayList<>();
            for (HashMap<String, Object> action : entry.getValue()) {
                if (action.containsKey("title"))
                    actions.add(new TitleAction(
                            action.get("title").toString(),
                            action.containsKey("subtitle") ? action.get("subtitle").toString() : ""
                    ));
                else if (action.containsKey("message"))
                    actions.add(new MessageAction(
                            action.get("message").toString(),
                            action.containsKey("broadcast") && (boolean) action.get("broadcast")
                    ));
                else if (action.containsKey("command"))
                    actions.add(new CommandAction(
                            action.get("command").toString()
                    ));
                else if (action.containsKey("sound"))
                    actions.add(new SoundAction(
                            action.get("sound").toString(),
                            action.containsKey("volume") ? Float.parseFloat(action.get("volume").toString()) : 1,
                            action.containsKey("pitch") ? Float.parseFloat(action.get("pitch").toString()) : 1
                    ));
            }
            rewards.put(entry.getKey(), actions);
        }

        return new Koth(
                (String) map.get("name"),
                (String) map.get("world-name"),
                (String) map.get("region-name"),
                ConfigBossBar.fromConfig((HashMap<String, Object>) map.get("boss-bar")),
                AutoRun.fromConfig((HashMap<String, Object>) map.get("auto-run")),
                (int) map.get("duration"),
                rewards
        );
    }

    private static class ModuleComparator implements Comparator<UUID> {
        private HashMap<UUID, Integer> scores;

        public ModuleComparator(HashMap<UUID, Integer> scores) {
            this.scores = scores;
        }

        @Override
        public int compare(UUID arg0, UUID arg1) {
            int destroys1 = this.scores.get(arg0);
            int destroys2 = this.scores.get(arg1);
            if (destroys1 < destroys2) {
                return 1;
            }
            if (destroys1 > destroys2) {
                return -1;
            }
            return 0;
        }
    }
}