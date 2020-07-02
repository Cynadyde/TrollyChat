package net.efcraft.trollychat.modifier;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class CynaChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    private final List<Character> VOWELS = new ArrayList<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y'));

    @Override
    public void accept(AsyncPlayerChatEvent event) {

        String format = event.getFormat();
        String playerName = event.getPlayer().getDisplayName();

        double rand = RNG.nextDouble();
        boolean inFront = rand > 0.5;

        if (playerName.length() <= 6) {
            if (inFront) {
                playerName = ChatColor.BLUE + "Cyna" + ChatColor.DARK_GRAY + playerName;
            }
            else {
                playerName = playerName + ChatColor.DARK_GRAY + "dyde";
            }
        }
        else {
            int halfPoint = playerName.length() / 2;
            if (VOWELS.contains(playerName.charAt(halfPoint))) {
                halfPoint += 1;
            }
            if (inFront) {
                playerName = ChatColor.BLUE + "Cyna" + ChatColor.DARK_GRAY + playerName.substring(halfPoint, playerName.length());
            }
            else {
                playerName = playerName.substring(0, halfPoint) + ChatColor.DARK_GRAY + "dyde";
            }
        }

        format = format.replaceFirst("%1\\$s", playerName + ChatColor.RESET);
        event.setFormat(format);

        if (rand < 0.005) {
            event.setMessage(event.getMessage() + ChatColor.GOLD + "  ... and thanks cyna for all you do for the server!!");
        }
    }
}
