package me.hexye.eliteLeaderboards.leaderboards;

import me.hexye.eliteLeaderboards.EliteLeaderboards;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Leaderboard {
    private String name;
    private String placeholderToTrack;
    private UUID leaderboardID;
    private HashMap<OfflinePlayer, Integer> scores = new HashMap<>();
    private List<OfflinePlayer> sortedPlayers = new ArrayList<>();
    private boolean transformToTime = false;

    public Leaderboard(String name, String placeholderToTrack, boolean transformToTime) {
        this.name = name;
        this.placeholderToTrack = placeholderToTrack;
        UUID localId = UUID.randomUUID();
        while (!EliteLeaderboards.getInstance().getLeaderboardManager().isFreeID(localId)) {
            localId = UUID.randomUUID();
        }
        leaderboardID = localId;
        this.transformToTime = transformToTime;
    }

    public Leaderboard(String name, String placeholderToTrack, UUID leaderboardID, boolean transformToTime) {
        this.name = name;
        this.placeholderToTrack = placeholderToTrack;
        this.leaderboardID = leaderboardID;
        this.transformToTime = transformToTime;
    }

    public String getName() {
        return name;
    }

    public String getPlaceholderToTrack() {
        return placeholderToTrack;
    }

    public UUID getID() {
        return leaderboardID;
    }

    public HashMap<OfflinePlayer, Integer> getScores() {
        return scores;
    }

    public List<OfflinePlayer> getSortedPlayers() {
        return sortedPlayers;
    }

    public void addScore(OfflinePlayer player, int score) {
        scores.put(player, score);
        sortedPlayers.remove(player);
        sortedPlayers.add(player);
        sortedPlayers.sort((p1, p2) -> Integer.compare(scores.getOrDefault(p2, 0), scores.getOrDefault(p1, 0)));
    }

    public Integer getScore(OfflinePlayer player) {
        return scores.getOrDefault(player, 0);
    }

    public Integer getPosition(OfflinePlayer player) {
        if (!sortedPlayers.contains(player)) {
            return null;
        }
        return sortedPlayers.indexOf(player) + 1;
    }

    public boolean isTransformToTime() {
        return transformToTime;
    }
}
