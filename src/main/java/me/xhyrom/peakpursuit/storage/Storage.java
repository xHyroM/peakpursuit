package me.xhyrom.peakpursuit.storage;

import me.xhyrom.peakpursuit.PeakPursuit;
import me.zort.sqllib.SQLConnectionBuilder;
import me.zort.sqllib.SQLDatabaseConnection;
import me.zort.sqllib.api.SQLEndpoint;
import org.bukkit.Bukkit;

public class Storage {
    public SQLDatabaseConnection connection;
    public String table;

    public Storage(
            String folder,
            String file,
            String table
    ) {
        this.connection = new SQLConnectionBuilder(
                new SQLEndpoint()  {
                    @Override
                    public String buildJdbc() {
                        return String.format("jdbc:sqlite:%s/%s", folder, file);
                    }

                    @Override
                    public String getUsername() {
                        return null;
                    }

                    @Override
                    public String getPassword() {
                        return null;
                    }
                }
        ).build();
        this.table = table;

        if (!connection.connect()) {
            PeakPursuit.getInstance().getLogger().warning("Failed to connect to the database!");
            Bukkit.getPluginManager().disablePlugin(PeakPursuit.getInstance());
        }
    }
}
