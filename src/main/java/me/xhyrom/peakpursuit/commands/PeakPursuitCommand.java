package me.xhyrom.peakpursuit.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import me.xhyrom.peakpursuit.PeakPursuit;
import me.xhyrom.peakpursuit.storage.structs.Votes;
import me.xhyrom.peakpursuit.structs.Koth;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PeakPursuitCommand {
    public static void register() {
        new CommandAPICommand("peakpursuit")
                .withPermission("peakpursuit.admin")
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(
                                new MultiLiteralArgument("name", List.of(PeakPursuit.getInstance().getKoths().keySet().toArray(new String[0])))
                        )
                        .executes(PeakPursuitCommand::start)
                )
                .withSubcommand(new CommandAPICommand("addvote")
                        .executes(PeakPursuitCommand::addVote))
                .register();
    }

    public static void start(CommandSender sender, CommandArguments args) {
        String kothName = (String) args.get(0);
        Koth koth = PeakPursuit.getInstance().getKoths().get(kothName);

        koth.start();
    }

    public static void addVote(CommandSender sender, CommandArguments args) {
        Bukkit.getScheduler().runTaskAsynchronously(PeakPursuit.getInstance(), () -> {
            Votes votes = PeakPursuit.getInstance().getStorage().connection
                    .select()
                    .from(PeakPursuit.getInstance().getStorage().table)
                    .where().isEqual("id", 1)
                    .obtainOne(Votes.class)
                    .orElse(new Votes(1, 0));

            votes.setVotes(votes.getVotes() + 1);
            PeakPursuit.getInstance().getStorage().votes = votes.getVotes();

            sender.sendMessage("Votes: " + votes.getVotes());

            PeakPursuit.getInstance().getStorage().connection
                    .save(PeakPursuit.getInstance().getStorage().table, votes).execute();

            for (Koth koth : PeakPursuit.getInstance().getKoths().values()) {
                if (koth.getAutoRun().votes.enabled) {
                    koth.getAutoRun().votes.start(koth, votes);
                }
            }
        });
    }
}
