package net.efcraft.trollychat.modifier;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public class LeetChatModifier implements Consumer<AsyncPlayerChatEvent> {

    @Override
    public void accept(AsyncPlayerChatEvent event) {
        event.setMessage(leetify(event.getMessage()));
    }

    public String leetify(String input) {

        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(" ").append(convertWord(word));
        }
        return result.toString();
    }

    private String convertWord(String word) {

        StringBuilder result = new StringBuilder();

        if (word.equalsIgnoreCase("you")) {
            result.append("J00");
        }
        else if (word.equalsIgnoreCase("dude")) {
            result.append("D00D");
        }
        else if (word.equalsIgnoreCase("elite")) {
            result.append("31337");
        }
        else {
            for (int i = 0; i < word.length(); i++) {

                /* if the current word contains 'ck' add "><0R5" else process char normally */
                if (i != (word.length() - 1)) {

                    /* checks last two letters of word */
                    if (i == (word.length() - 2)) {

                        if (word.substring(i).equalsIgnoreCase("ck")) {
                            result.append("><0R5");
                            i++;
                            continue;
                        }
                    }
                    else {
                        if (word.substring(i, i + 2).equalsIgnoreCase("ck")) {
                            result.append("><0R");
                            i++;
                            continue;
                        }
                    }
                }
                switch (word.charAt(i)) {
                    case 'a':
                    case 'A':
                        result.append("4");
                        break;
                    case 'e':
                    case 'E':
                        result.append("3");
                        break;
                    case 'i':
                    case 'I':
                        result.append("1");
                        break;
                    case 'o':
                    case 'O':
                        result.append("0");
                        break;
                    case 'g':
                    case 'G':
                        result.append("9");
                        break;
                    case 'h':
                    case 'H':
                        result.append("|-|");
                        break;
                    case 'v':
                    case 'V':
                        result.append("\\/");
                        break;
                    case 'w':
                    case 'W':
                        result.append("\\/\\/");
                        break;
                    case 'm':
                    case 'M':
                        result.append("/\\/\\");
                        break;
                    case 'k':
                    case 'K':
                        result.append("|<");
                        break;
                    case 's':
                    case 'S':
                        result.append("5");
                        break;
                    case 't':
                    case 'T':
                        result.append("7");
                        break;
                    case 'z':
                    case 'Z':
                        result.append("2");
                        break;
                    default:
                        result.append(word.charAt(i));
                } //switch statement
            } //for
        } //else

        return result.toString();
    }
}
