package net.efcraft.trollychat.modifier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class RandomizerChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    @Override
    public void accept(AsyncPlayerChatEvent event) {

        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        int rand = RNG.nextInt(players.size());

        event.setFormat(event.getFormat().replaceFirst(
                "%1\\$s",
                players.get(rand).getDisplayName()));
    }
}
