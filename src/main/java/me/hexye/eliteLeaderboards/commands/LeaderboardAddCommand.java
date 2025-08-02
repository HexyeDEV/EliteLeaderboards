package me.hexye.eliteLeaderboards.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.hexye.eliteLeaderboards.EliteLeaderboards;
import me.hexye.eliteLeaderboards.leaderboards.Leaderboard;
import me.hexye.eliteLeaderboards.leaderboards.LeaderboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LeaderboardAddCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length < 2 || strings.length > 3) {
            return false;
        }
        String name = strings[0];
        String placeholder = strings[1];
        boolean transformToTime = false;
        if (strings.length == 3) {
            String toTimeArg = strings[2].toLowerCase();
            if (toTimeArg.equals("y")) {
                transformToTime = true;
            }
        }
        Leaderboard leaderboard = new Leaderboard(name, placeholder, transformToTime);
        LeaderboardManager leaderboardManager = EliteLeaderboards.getInstance().getLeaderboardManager();
        leaderboardManager.addLeaderboard(leaderboard);
        commandSender.sendMessage("Leaderboard " + name + " has been added, tracking the placeholder: " + placeholder);
        return true;
    }
}
