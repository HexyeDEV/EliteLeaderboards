package me.hexye.eliteLeaderboards.database;

import me.hexye.eliteLeaderboards.EliteLeaderboards;
import me.hexye.eliteLeaderboards.leaderboards.Leaderboard;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.UUID;

public class Database {
    private YamlConfiguration leaderboards;

    public void initialize() {
        leaderboards = YamlConfiguration.loadConfiguration(
                new java.io.File(EliteLeaderboards.getInstance().getDataFolder(), "leaderboards.yml")
        );
    }

    public void addLeaderboard(Leaderboard leaderboard) {
        String id = leaderboard.getID().toString();
        String name = leaderboard.getName();
        String placeholderToTrack = leaderboard.getPlaceholderToTrack();
        boolean transformToTime = leaderboard.isTransformToTime();

        leaderboards.set(id + ".name", name);
        leaderboards.set(id + ".placeholder", placeholderToTrack);
        leaderboards.set(id + ".transformToTime", transformToTime);
        try {
            leaderboards.save(new java.io.File(EliteLeaderboards.getInstance().getDataFolder(), "leaderboards.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeLeaderboard(Leaderboard leaderboard) {
        String id = leaderboard.getID().toString();
        leaderboards.set(id, null);
        try {
            leaderboards.save(new java.io.File(EliteLeaderboards.getInstance().getDataFolder(), "leaderboards.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<UUID, Leaderboard> getLeaderboards() {
        HashMap<UUID, Leaderboard> leaderboardMap = new HashMap<>();
        for (String key : leaderboards.getKeys(false)) {
            UUID id = UUID.fromString(key);
            String name = leaderboards.getString(key + ".name");
            String placeholderToTrack = leaderboards.getString(key + ".placeholder");
            boolean transformToTime = leaderboards.getBoolean(key + ".transformToTime", false);
            Leaderboard leaderboard = new Leaderboard(name, placeholderToTrack, id, transformToTime);
            leaderboardMap.put(id, leaderboard);
        }
        return leaderboardMap;
    }
}
