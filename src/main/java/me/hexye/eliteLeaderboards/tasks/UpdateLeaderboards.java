package me.hexye.eliteLeaderboards.tasks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.hexye.eliteLeaderboards.EliteLeaderboards;
import me.hexye.eliteLeaderboards.leaderboards.Leaderboard;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

public class UpdateLeaderboards extends BukkitRunnable {
    @Override
    public void run() {
        List<Leaderboard> leaderboards = EliteLeaderboards.getInstance().getLeaderboardManager().getLeaderboards();
        Set<OfflinePlayer> players = EliteLeaderboards.getInstance().getPlayers();
        for (Leaderboard leaderboard : leaderboards) {
            String placeholder = leaderboard.getPlaceholderToTrack();
            if (placeholder == null || placeholder.isEmpty()) {
                continue;
            }
            for (OfflinePlayer player : players) {
                String value = PlaceholderAPI.setPlaceholders(player, placeholder);
                if (value.equals("...") || value.equals(".") || value.equals("..") || value.equals("No player")) {
                    continue;
                }
                int intValue;
                try {
                    intValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    EliteLeaderboards.getInstance().getLogger().warning("Invalid value for placeholder '" + placeholder + "' for player '" + player.getName() + "': " + value);
                    continue;
                }
                leaderboard.addScore(player, intValue);
            }
        }
    }
}
