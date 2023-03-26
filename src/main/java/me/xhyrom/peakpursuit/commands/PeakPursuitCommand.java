package me.xhyrom.peakpursuit.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.xhyrom.peakpursuit.PeakPursuit;
import me.xhyrom.peakpursuit.structs.Koth;
import org.bukkit.command.CommandSender;

public class PeakPursuitCommand {
    public static void register() {
        new CommandAPICommand("peakpursuit")
                .withPermission("peakpursuit.admin")
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(
                                new MultiLiteralArgument(PeakPursuit.getInstance().getKoths().keySet().toArray(new String[0]))
                        )
                        .executes(PeakPursuitCommand::start)
                )
                .register();
    }

    public static void start(CommandSender sender, Object[] args) {
        String kothName = (String) args[0];
        Koth koth = PeakPursuit.getInstance().getKoths().get(kothName);

        koth.start();
    }
}
