package net.efcraft.trollychat.modifier;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;
import java.util.function.Consumer;

public class RainbowChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    private final ChatColor[] COLORS = new ChatColor[] {
            ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA,
            ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE,
            ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW
    };

    @Override
    public void accept(AsyncPlayerChatEvent event) {
        event.setMessage(rainbowize(event.getMessage()));
    }

    public String rainbowize(String input) {

        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            int rand = RNG.nextInt(COLORS.length);
            result.append(COLORS[rand]).append(word).append(" ");
        }
        return result.toString();
    }
}
