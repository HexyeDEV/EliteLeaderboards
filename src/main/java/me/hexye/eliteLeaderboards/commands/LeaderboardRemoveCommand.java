package me.hexye.eliteLeaderboards.commands;

import me.hexye.eliteLeaderboards.EliteLeaderboards;
import me.hexye.eliteLeaderboards.leaderboards.Leaderboard;
import me.hexye.eliteLeaderboards.leaderboards.LeaderboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LeaderboardRemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length != 1) {
            return false;
        }
        String name = strings[0];
        LeaderboardManager leaderboardManager = EliteLeaderboards.getInstance().getLeaderboardManager();
        Leaderboard leaderboard = leaderboardManager.getLeaderboardByName(name);
        if (leaderboard == null) {
            commandSender.sendMessage("Leaderboard " + name + " does not exist.");
            return true;
        }
        leaderboardManager.removeLeaderboard(leaderboard);
        commandSender.sendMessage("Leaderboard " + name + " has been removed.");
        return true;
    }
}
