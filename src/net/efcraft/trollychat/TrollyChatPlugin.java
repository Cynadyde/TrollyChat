package net.efcraft.trollychat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The plugin's main class.
 */
public class TrollyChatPlugin extends JavaPlugin implements Listener {

    private static TrollyChatPlugin instance;

    private final String CHAT_TAG = "§0[§6EF§0]§r ";

    private PluginCommand chatModeCmd;
    private List<ChatMode> activatedModes;
    private Map<Player, List<ChatMode>> targetActivatedModes;

    public static @NotNull TrollyChatPlugin getInstance() {
        if (instance == null) {
            throw new IllegalStateException("plugin not yet enabled!");
        }
        return instance;
    }

    @Override
    public void onEnable() {

        chatModeCmd = Objects.requireNonNull(getCommand("chatmode"));
        chatModeCmd.setExecutor(this);
        chatModeCmd.setTabCompleter(this);

        activatedModes = new ArrayList<>();
        targetActivatedModes = new HashMap<>();

        getServer().getPluginManager().registerEvents(this, this);
        instance = this;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args) {

        executeCmd:
        if (command == chatModeCmd) {

            ChatMode chatMode;
            Player target = null;
            boolean silent = false;

            if (args.length < 1 || args.length > 3) {
                sendMsg(sender, "§cUsage: §6/chatmode <mode> [<player>] [-s(ilent)]");
                if (args.length == 0) {
                    sendMsg(sender, "§3Available chat modes: §e" + Arrays.stream(ChatMode.values())
                            .map(mode -> (isModeActivated(mode) ? "§a" : "§e") + (isModeActivatedForAny(mode) ? "§o" : "") + mode.getLabel())
                            .collect(Collectors.joining("§e, ")));
                }
                break executeCmd;
            }
            chatMode = Arrays.stream(ChatMode.values())
                    .filter(mode -> mode.getLabel().equalsIgnoreCase(args[0]))
                    .findFirst().orElse(null);

            if (chatMode == null) {
                sendMsg(sender, "§cNo such chat mode: §6" + args[0]);
                break executeCmd;
            }
            if (!sender.hasPermission(chatMode.getPermission())) {
                sendMsg(sender, "§cYou don't have permission to do that!");
                break executeCmd;
            }
            if (args.length >= 2) {

                if (args[1].startsWith("-")) {
                    if ("-silent".startsWith(args[1].toLowerCase())) {
                        silent = true;
                    }
                    else {
                        sendMsg(sender, "§cUnknown flag given: §6" + args[1]);
                        break executeCmd;
                    }
                }
                else {
                    target = getServer().getPlayer(args[1]);

                    if (target == null) {
                        sendMsg(sender, "§cInvalid player: §6" + args[1]);
                        break executeCmd;
                    }

                    if (args.length >= 3) {
                        if ("-silent".startsWith(args[2].toLowerCase())) {
                            silent = true;
                        }
                        else {
                            sendMsg(sender, "§cUnknown flag given: §6" + args[2]);
                            break executeCmd;
                        }
                    }
                }
            }
            if (target == null) {
                toggleMode(chatMode);
                if (!silent) {
                    broadcastMsg(chatMode.getStatusMsg(isModeActivated(chatMode)));
                }
            }
            else {
                toggleModeFor(target, chatMode);
                if (!silent) {
                    broadcastMsg(chatMode.getStatusMsg(isModeActivatedFor(target, chatMode), target));
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args) {

        List<String> results = new ArrayList<>();
        if (command == chatModeCmd) {

            if (args.length == 0) {
                results.addAll(Arrays.stream(ChatMode.values())
                        .filter(mode -> sender.hasPermission(mode.getPermission()))
                        .map(ChatMode::getLabel)
                        .collect(Collectors.toList()));
            }
            else if (args.length == 1) {
                String modeArg = args[0].toLowerCase();
                results.addAll(Arrays.stream(ChatMode.values())
                        .filter(mode -> sender.hasPermission(mode.getPermission()))
                        .map(ChatMode::getLabel)
                        .filter(label -> label.startsWith(modeArg))
                        .collect(Collectors.toList()));
            }
            else if (args.length == 2) {
                String playerArg = args[1].toLowerCase();
                results.addAll(getServer().getOnlinePlayers().stream()
                        .map(HumanEntity::getName)
                        .filter(name -> name.toLowerCase().startsWith(playerArg))
                        .collect(Collectors.toList()));
            }
            if (args.length == 2 || args.length == 3) {
                if ("-silent".startsWith(args[1].toLowerCase())) {
                    results.add("-s");
                }
            }
        }
        return results;
    }

    /**
     * Passes player chat events to any activated {@link ChatMode ChatModes}.
     *
     * @param event the event passed from the spigot plugin manager.
     */
    @EventHandler
    public void onAsyncPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        for (ChatMode mode : activatedModes) {
            mode.applyChatTroll(event);
        }
        for (ChatMode mode : targetActivatedModes.getOrDefault(event.getPlayer(), Collections.emptyList())) {
            if (!activatedModes.contains(mode)) {
                mode.applyChatTroll(event);
            }
        }
    }

    /**
     * Sends a message, formatted by the plugin, through chat.
     *
     * @param receiver the receiver of the message
     * @param message  the message to be seen by the recipient
     */
    public void sendMsg(CommandSender receiver, String message) {
        receiver.sendMessage(CHAT_TAG + message);
    }

    /**
     * Broadcasts a message, formatted by the plugin, through chat.
     *
     * @param message the message to be seen by the whole server
     */
    public void broadcastMsg(String message) {
        getServer().broadcastMessage(CHAT_TAG + message);
    }

    /**
     * Checks if the given chat mode is activated.
     *
     * @param mode the specified chat mode
     * @return true if that chat mode is activated
     */
    public boolean isModeActivated(@NotNull ChatMode mode) {
        return activatedModes.contains(mode);
    }

    /**
     * Checks if the given chat mode is specifically activated for a target
     *
     * @param target the target player
     * @param mode   the specified chat mode
     * @return true if that chat mode is activated for the player
     */
    public boolean isModeActivatedFor(@NotNull Player target, @NotNull ChatMode mode) {
        return targetActivatedModes.getOrDefault(target, Collections.emptyList()).contains(mode);
    }

    /**
     * Checks if the given chat mode is specifically activated for any targets.
     *
     * @param mode the specified chat mode
     * @return true if the chat mode is activated for any players
     */
    public boolean isModeActivatedForAny(@NotNull ChatMode mode) {
        return targetActivatedModes.values().stream().anyMatch(list -> list.contains(mode));
    }

    /**
     * Toggles the given chat mode.
     *
     * @param mode the specified chat mode
     */
    public void toggleMode(@NotNull ChatMode mode) {
        if (activatedModes.contains(mode)) {
            activatedModes.remove(mode);
        }
        else {
            activatedModes.add(mode);
        }
    }

    /**
     * Toggles the given chat mod specifically for a target.
     *
     * @param target the target player
     * @param mode   the specified chat mode
     */
    public void toggleModeFor(@NotNull Player target, @NotNull ChatMode mode) {
        if (targetActivatedModes.getOrDefault(target, Collections.emptyList()).contains(mode)) {
            List<ChatMode> modes = targetActivatedModes.get(target);
            modes.remove(mode);
            if (modes.isEmpty()) {
                targetActivatedModes.remove(target);
            }
        }
        else {
            if (!targetActivatedModes.containsKey(target)) {
                targetActivatedModes.put(target, new ArrayList<>());
            }
            targetActivatedModes.get(target).add(mode);
        }
    }
}
