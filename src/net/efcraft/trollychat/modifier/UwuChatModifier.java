package net.efcraft.trollychat.modifier;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;
import java.util.function.Consumer;

public class UwuChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    @Override
    public void accept(AsyncPlayerChatEvent event) {
        event.setMessage(makeUwu(event.getMessage()));
    }

    public String makeUwu(String text) {

        // TODO apply UwU research to this function

        return text
                .replaceAll("r", "w")
                .replaceAll("oo", "owo")
                .replaceAll("ou", "uwu")
                .replaceAll("th", "d")
                .replaceAll("l", "w");
    }
}
