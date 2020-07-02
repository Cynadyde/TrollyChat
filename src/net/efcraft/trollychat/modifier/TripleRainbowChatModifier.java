package net.efcraft.trollychat.modifier;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;
import java.util.function.Consumer;

public class TripleRainbowChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    private final ChatColor[] COLORS = new ChatColor[] {
            ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA,
            ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE,
            ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW
    };

    private final ChatColor[] FORMATS = new ChatColor[] {
            ChatColor.ITALIC, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, null
    };

    @Override
    public void accept(AsyncPlayerChatEvent event) {
        event.setMessage(rainbowize(event.getMessage()));
    }

    public String rainbowize(String input) {

        char[] chars = input.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char ch : chars) {
            ChatColor color = COLORS[RNG.nextInt(COLORS.length)];
            ChatColor format = FORMATS[RNG.nextInt(FORMATS.length)];

            result.append(color);
            if (format != null) {
                result.append(format);
            }
            result.append(ch);
        }
        return result.toString();
    }
}
