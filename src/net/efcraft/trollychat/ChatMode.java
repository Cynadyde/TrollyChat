package net.efcraft.trollychat;

import net.efcraft.trollychat.modifier.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IllegalFormatException;
import java.util.function.Consumer;

/**
 * An enumeration of the various trolly chat modes.
 */
public enum ChatMode {

    /* add additional chat modes down below!

       IMPORTANT: the activate/deactivate messages should
       contain a single %s, while any other %'s should be escaped (%%). */

    RAINBOW(
            "§6Rainbow Mode: ACTIVATED%s",
            "§6Rainbow Mode: DEACTIVATED%s",
            new Permission("efcraft.chatmode.rainbow"),
            new RainbowChatModifier()
    ),

    DOUBLE_RAINBOW(
            "§6DOUBLE Rainbow Mode: ACTIVATED%s",
            "§6DOUBLE Rainbow Mode: DEACTIVATED%s",
            new Permission("efcraft.chatmode.doublerainbow"),
            new DoubleRainbowChatModifier()
    ),

    TRIPLE_RAINBOW(
            "§6TRIPLE Rainbow mode!$#!?$?@#$%%@!: ACTIVATED%s",
            "§6TRIPLE Rainbow mode!$#!?$?@#$%%@!: DEACTIVATED%s",
            new Permission("efcraft.chatmode.triplerainbow"),
            new TripleRainbowChatModifier()
    ),

    LEET(
            "§6Leet Mode: ACTIVATED%s",
            "§6Leet Mode: DEACTIVATED%s",
            new Permission("efcraft.chatmode.leet"),
            new LeetChatModifier()
    ),

    RANDOMIZER(
            "§6Randomizer Mode: ACTIVATED%s",
            "§6Randomizer Mode: DEACTIVATED%s",
            new Permission("efcraft.chatmode.randomizer"),
            new RandomizerChatModifier()
    ),

    BREW(
            "§6Brew Mode: ACTIVATED%s",
            "§6Brew Mode: DEACTIVATED%s",
            new Permission("efcraft.chatmode.brew"),
            new BrewChatModifier()
    ),

    CYNA(
            "§6Cyna Mode: ACTIVATED%s",
            "§6Cyna Mode: DEACTIVATED%s",
            new Permission("efcraft.chatmode.cyna"),
            new CynaChatModifier()
    ),

    MOIST(
            "§3The server is now MOIST%s§3...",
            "§6MOIST MODE DEACTIVATED%s",
            new Permission("efcraft.chatmode.moist"),
            new MoistChatModifier()
    ),

    UWU(
            "§dTeh servowr iz now vewwy UwU%s§d <333 x3 o3o",
            "§6UWU MODE DEACTIVATED%s",
            new Permission("efcraft.chatmode.uwu"),
            new UwuChatModifier()
    );

    private String activationMsg;
    private String deactivationMsg;
    private Permission permission;
    private Consumer<AsyncPlayerChatEvent> chatHandler;

    ChatMode(
            @NotNull String activationMsg,
            @NotNull String deactivationMsg,
            @NotNull Permission permission,
            @NotNull Consumer<AsyncPlayerChatEvent> chatHandler) {

        this.activationMsg = activationMsg;
        this.deactivationMsg = deactivationMsg;
        this.permission = permission;
        this.chatHandler = chatHandler;
    }

    /**
     * Gets the label for this chat mode's command.
     *
     * @return the command label
     */
    public String getLabel() {
        return name().toLowerCase().replace("_", "");
    }

    /**
     * Gets this chat mode's permission node.
     *
     * @return the permission required to toggle this chat mode
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Gets this chat mode's status message.
     *
     * @param activated whether to get the activation or the deactivation message
     * @return a descriptive message
     *
     * @see #getStatusMsg(boolean, Player)
     */
    public String getStatusMsg(boolean activated) {
        return getStatusMsg(activated, null);
    }

    /**
     * Gets this chat mode's status message.
     *
     * @param activated whether to get the activation or the deactivation message
     * @param target    (optional) the activation target
     * @return a descriptive message
     *
     * @see #getStatusMsg(boolean)
     */
    public String getStatusMsg(boolean activated, @Nullable Player target) {
        try {
            return String.format(
                    activated ? activationMsg : deactivationMsg,
                    target == null ? "" : " for §e" + target.getDisplayName());
        }
        catch (IllegalFormatException ex) {
            return (activated ? activationMsg : deactivationMsg) +
                    (target == null ? "" : " for &e" + target.getDisplayName());
        }
    }

    /**
     * Modifies a player chat event in some trollish way.
     * <p>
     * Since this method is called asynchronously, the Bukkit API should NOT be used!
     *
     * @param event the chat event passed from {@link TrollyChatPlugin}
     */
    public void applyChatTroll(AsyncPlayerChatEvent event) {
        chatHandler.accept(event);
    }
}
