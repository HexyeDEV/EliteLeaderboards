package me.hexye.eliteLeaderboards.leaderboards;

import me.hexye.eliteLeaderboards.EliteLeaderboards;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LeaderboardManager {
    private final HashMap<UUID, Leaderboard> leaderboards = new HashMap<>();

    public LeaderboardManager(HashMap<UUID, Leaderboard> loadedLeaderboards) {
        if (loadedLeaderboards != null) {
            this.leaderboards.putAll(loadedLeaderboards);
        }
    }

    public List<Leaderboard> getLeaderboards() {
        return new ArrayList<>(leaderboards.values());
    }

    public void addLeaderboard(Leaderboard leaderboard) {
        if (leaderboard == null || leaderboard.getID() == null) {
            throw new IllegalArgumentException("Leaderboard or its ID cannot be null");
        }
        leaderboards.put(leaderboard.getID(), leaderboard);
        new BukkitRunnable() {
            @Override
            public void run() {
                EliteLeaderboards.getInstance().getDatabase().addLeaderboard(leaderboard);
            }
        }.runTaskAsynchronously(EliteLeaderboards.getInstance());
    }

    public void removeLeaderboard(Leaderboard leaderboard) {
        leaderboards.remove(leaderboard.getID());
        new BukkitRunnable() {
            @Override
            public void run() {
                EliteLeaderboards.getInstance().getDatabase().removeLeaderboard(leaderboard);
            }
        }.runTaskAsynchronously(EliteLeaderboards.getInstance());
    }

    public boolean isFreeID(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return !leaderboards.containsKey(id);
    }

    public Leaderboard getLeaderboard(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return leaderboards.get(id);
    }

    public Leaderboard getLeaderboardByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        for (Leaderboard leaderboard : leaderboards.values()) {
            if (leaderboard.getName().equalsIgnoreCase(name)) {
                return leaderboard;
            }
        }
        return null;
    }
}
