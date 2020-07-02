package net.efcraft.trollychat.modifier;

import net.efcraft.trollychat.TrollyChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;
import java.util.function.Consumer;

public class BrewChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    private String[] brewQuestions = {
            "can you make it so eather brewing or diamonds gets added to peoples names?",
            "wheres the entrance to the black market?",
            "how do i get guns?",
            "did u see me?",
            "are you a girl?",
            "can we get married plz?",
            "what is your skype?",
            "can you talk?",
            "can we plz get married now?",
            "Hey BrewingDiamonds, TP to me?",
            "tpa to me brewing?",
            "can i have materials?",
            "do u like it?",
            "wanna pvp?",
            "i need money?",
            "stay where you are",
            "sure you were",
            "lets date instead?",
            "tpa tp me?",
            "hey brewing tp to me?",
            "tpa brewing?",
            "get me another sword mine wore off",
            "what?",
            "what??",
            "what u mean?",
            "what do you mean brewing?",
            "i need money",
            "can i you make my house for me?",
            "how much diamond do you have?",
            "tp i want you to look at my house",
            "will you marry me in game?",
            "marry me? yes or no",
            "do you have any spare looting 3?",
            "what",
            "pvp now",
            "brewing pvp me",
            "brewing tpa me",
            "are you a girl?",
            "stop bothering me",
            "stay where you are",
            "dont move",
            "brewing i need ice spikes biome"
    };

    @Override
    public void accept(AsyncPlayerChatEvent event) {

        Bukkit.getServer().getScheduler().runTaskLater(
                TrollyChatPlugin.getInstance(), () -> sendBrewQuestion(event.getPlayer()), 1L);
    }

    public void sendBrewQuestion(Player player) {

        int rand = RNG.nextInt(brewQuestions.length);
        Bukkit.getServer().dispatchCommand(player, "msg BrewingDiamonds " + brewQuestions[rand]);
    }
}
