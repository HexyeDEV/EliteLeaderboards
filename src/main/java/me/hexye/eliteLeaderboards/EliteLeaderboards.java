package me.hexye.eliteLeaderboards;

import me.hexye.eliteLeaderboards.commands.LeaderboardAddCommand;
import me.hexye.eliteLeaderboards.commands.LeaderboardRemoveCommand;
import me.hexye.eliteLeaderboards.database.Database;
import me.hexye.eliteLeaderboards.events.PlayerJoin;
import me.hexye.eliteLeaderboards.leaderboards.LeaderboardManager;
import me.hexye.eliteLeaderboards.tasks.UpdateLeaderboards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class EliteLeaderboards extends JavaPlugin {
    Set<OfflinePlayer> players = new java.util.HashSet<>();
    LeaderboardManager leaderboardManager;
    Database database;
    FileConfiguration config;

    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().severe("PlaceholderAPI is not installed! Disabling EliteLeaderboards...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        config = getConfig();

        getCommand("leaderboardadd").setExecutor(new LeaderboardAddCommand());
        getCommand("leaderboardremove").setExecutor(new LeaderboardRemoveCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

        getLogger().info("Getting all players...");
        OfflinePlayer[] found = getServer().getOfflinePlayers();
        getLogger().info("Found " + found.length + " players.");
        for (OfflinePlayer player : found) {
            if (player.hasPlayedBefore()) {
                players.add(player);
            }
        }
        getLogger().info("Added " + players.size() + " players to the players set.");
        database = new Database();
        database.initialize();
        leaderboardManager = new LeaderboardManager(database.getLeaderboards());
        new PlaceholderExpansion().register();
        getLogger().info("Registered PlaceholderAPI expansion.");
        new UpdateLeaderboards().runTaskTimerAsynchronously(this, 0L, config.getInt("update-every")*20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Set<OfflinePlayer> getPlayers() {
        return players;
    }

    public void newPlayer(OfflinePlayer player) {
        players.add(player);
    }

    public static EliteLeaderboards getInstance() {
        return JavaPlugin.getPlugin(EliteLeaderboards.class);
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public Database getDatabase() {
        return database;
    }

    public int playersPerPage() {
        return config.getInt("players-per-page", 10);
    }

    public String positionMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("position-message"));
    }
}
