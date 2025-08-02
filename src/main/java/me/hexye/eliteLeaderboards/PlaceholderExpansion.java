package me.hexye.eliteLeaderboards;

import me.hexye.eliteLeaderboards.leaderboards.Leaderboard;
import me.hexye.eliteLeaderboards.leaderboards.LeaderboardManager;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "Hexye";
    }

    @Override
    public String getIdentifier() {
        return "eliteleaderboards";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer ownPlayer, String identifier) {
        String[] parts = identifier.split("_");
        String name = parts[0];
        String type = parts[1];
        if (type.equals("own")) {
            LeaderboardManager leaderboardManager = EliteLeaderboards.getInstance().getLeaderboardManager();
            Leaderboard leaderboard = leaderboardManager.getLeaderboardByName(name);
            if (leaderboard == null) {
                return "";
            }
            int score = leaderboard.getScore(ownPlayer);
            int position = leaderboard.getPosition(ownPlayer);
            String positionMessage = EliteLeaderboards.getInstance().positionMessage();
            if (leaderboard.isTransformToTime()) {
                int days = score / 86400;
                int hours = (score % 86400) / 3600;
                int minutes = (score % 3600) / 60;
                int seconds = score % 60;
                String time = (days > 0 ? days + "d " : "") +
                              (hours > 0 ? hours + "h " : "") +
                              (minutes > 0 ? minutes + "m " : "") +
                              (seconds > 0 ? seconds + "s" : "");
                if (time.isEmpty()) {
                    time = "0s";
                }
                return positionMessage.replace("{position}", String.valueOf(position))
                        .replace("{player}", ownPlayer.getName() != null ? ownPlayer.getName() : "Unknown")
                        .replace("{score}", time);
            } else {
                return positionMessage.replace("{position}", String.valueOf(position))
                        .replace("{player}", ownPlayer.getName() != null ? ownPlayer.getName() : "Unknown")
                        .replace("{score}", String.valueOf(score));
            }
        }
        int number = Integer.valueOf(parts[2]);
        String positionMessage = EliteLeaderboards.getInstance().positionMessage();
        Leaderboard leaderboard = EliteLeaderboards.getInstance().getLeaderboardManager().getLeaderboardByName(name);
        if (leaderboard == null) {
            return "";
        }
        if (type.equals("page")) {
            List<OfflinePlayer> sortedPlayers = leaderboard.getSortedPlayers();
            int playersPerPage = EliteLeaderboards.getInstance().playersPerPage();
            int pageCount = (int) Math.ceil((double) sortedPlayers.size() / playersPerPage);
            if (number < 1 || number > pageCount) {
                return "";
            }
            int startIndex = (number - 1) * playersPerPage;
            int endIndex = Math.min(startIndex + playersPerPage, sortedPlayers.size());
            StringBuilder result = new StringBuilder();
            for (int i = startIndex; i < endIndex; i++) {
                OfflinePlayer p = sortedPlayers.get(i);
                String playerName = p.getName() != null ? p.getName() : "Unknown";
                int score = leaderboard.getScore(p);
                int position = i + 1;
                String time;
                if (leaderboard.isTransformToTime()) {
                    int days = score / 86400;
                    int hours = (score % 86400) / 3600;
                    int minutes = (score % 3600) / 60;
                    int seconds = score % 60;
                    time = (days > 0 ? days + "d " : "") +
                           (hours > 0 ? hours + "h " : "") +
                           (minutes > 0 ? minutes + "m " : "") +
                           (seconds > 0 ? seconds + "s" : "");
                    if (time.isEmpty()) {
                        time = "0s";
                    }
                    result.append(positionMessage.replace("{position}", String.valueOf(position))
                                    .replace("{player}", playerName)
                                    .replace("{score}", time))
                            .append("\n");
                } else {
                    result.append(positionMessage.replace("{position}", String.valueOf(position))
                                    .replace("{player}", playerName)
                                    .replace("{score}", String.valueOf(score)))
                            .append("\n");
                }
            }
            if (result.length() > 0) {
                result.setLength(result.length() - 1); // Remove the last newline character
            }
            return result.toString();
        } else if (type.equals("position")) {
            List<OfflinePlayer> sortedPlayers = leaderboard.getSortedPlayers();
            if (number < 0 || number > sortedPlayers.size()) {
                return "Not ranked";
            }
            OfflinePlayer player1 = sortedPlayers.get(number - 1);
            String playerName = player1.getName() != null ? player1.getName() : "Unknown";
            int score = leaderboard.getScore(player1);
            if (leaderboard.isTransformToTime()) {
                int days = score / 86400;
                int hours = (score % 86400) / 3600;
                int minutes = (score % 3600) / 60;
                int seconds = score % 60;
                String time = (days > 0 ? days + "d " : "") +
                              (hours > 0 ? hours + "h " : "") +
                              (minutes > 0 ? minutes + "m " : "") +
                              (seconds > 0 ? seconds + "s" : "");
                if (time.isEmpty()) {
                    time = "0s";
                }
                return positionMessage.replace("{position}", String.valueOf(number))
                        .replace("{player}", playerName)
                        .replace("{score}", time);
            } else {
                return positionMessage.replace("{position}", String.valueOf(number))
                        .replace("{player}", playerName)
                        .replace("{score}", String.valueOf(score));
            }
        } else {
            return "Invalid type";
        }
    }

    @Override
    public boolean persist() {
        return true; // This allows the expansion to be persistent across server restarts
    }
}
