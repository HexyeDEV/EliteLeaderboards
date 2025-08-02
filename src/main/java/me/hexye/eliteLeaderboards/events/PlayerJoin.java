package me.hexye.eliteLeaderboards.events;

import me.hexye.eliteLeaderboards.EliteLeaderboards;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        OfflinePlayer player = event.getPlayer();
        EliteLeaderboards.getInstance().newPlayer(player);
    }
}
