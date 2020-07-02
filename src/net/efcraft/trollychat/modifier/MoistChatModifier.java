package net.efcraft.trollychat.modifier;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MoistChatModifier implements Consumer<AsyncPlayerChatEvent> {

    private final Random RNG = new Random();

    private final List<Character> BL = new ArrayList<>(Arrays.asList('w', 'y', 't'));  // blacklisted 'first letter's
    private final List<Character> VOWELS = new ArrayList<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y'));
    private final char[] MOIST = "moist".toCharArray();

    @Override
    public void accept(AsyncPlayerChatEvent event) {
        event.setMessage(moisturize(event.getMessage()));
    }

    /**
     * Makes the given message very moist.
     * If all goes according to plan, everyone will feel very uncomfortable.
     *
     * @param input the input message
     * @return the moist version of the message
     */
    public String moisturize(String input) {

        List<Character> chars = input.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        int i; // word start (inclusive)
        int t = -1; // word end (exclusive)

        moistify:
        while (true) {

            // 1. find the next word in the message
            i = t;
            do {
                i++;
                if (i >= chars.size()) {
                    break moistify;
                }
            }
            while (!isCharWordPart(chars.get(i)));

            t = i;
            while (t < chars.size() && isCharWordPart(chars.get(t))) {
                t++;
            }

            // 2. if word is 2 or less chars, skip it!
            if (t - i <= 2) {
                continue;
            }

            // 3. if word starts with blacklisted chars or already IS moist, skip it!
            if (BL.contains(Character.toLowerCase(chars.get(i)))) {
                continue;
            }
            if (chars.subList(i, t).stream()
                    .map(Character::toLowerCase)
                    .map(String::valueOf)
                    .collect(Collectors.joining())
                    .contains("moist")) {
                continue;
            }

            // 4. five percent of the time, skip as well!
            if (RNG.nextDouble() <= 0.05) {
                continue;
            }

            // 5. find the furthest vowel, no more than 8 chars away, from
            // the end (but never the last char), yet no closer than 4 chars to
            // the beginning, and use this partition (to the end of the word)
            // as a suffix.
            List<Character> suffix = Collections.emptyList();

            int j = -1; // vowel index
            int k = t - 2; // vowel seeker
            while ((k - i) >= 4 && (t - k) <= 8) {
                k--;
                if (VOWELS.contains(Character.toLowerCase(chars.get(k)))) {
                    j = k;
                }
            }
            if (j != -1) {
                suffix = new ArrayList<>(chars.subList(j, t));
            }

            // 6. wherever there are more than 2 duplicate letters in a row,
            // calculate which letters of "moist" should equivalently be duplicated.

            // MAGIC VALUE: moist has 5 letters
            Map<Integer, Integer> moistCounts = new HashMap<>();
            for (int g = 0; g < 5; g++) moistCounts.put(g, 1);

            LinkedHashMap<Integer, Integer> wordCounts = new LinkedHashMap<>();
            int g = 0; // letter index as map key
            char ch = Character.toLowerCase(chars.get(i)); // current char
            char next; // next char for comparison
            int amount = 1; // current char amount

            for (int h = i + 1; h < t; h++) {
                next = Character.toLowerCase(chars.get(h));
                if (next == ch) {
                    amount++;
                }
                else {
                    wordCounts.put(g++, amount);
                    ch = next;
                    amount = 1;
                }
            }
            elongate:
            if (!wordCounts.isEmpty()) {

                // take care of the first letter
                int firstAmt = wordCounts.remove(0);
                if (firstAmt > 2) {
                    moistCounts.put(0, firstAmt);
                }
                if (wordCounts.size() < 2) {
                    break elongate;
                }
                // take care of the last letter
                int lastAmt = wordCounts.remove(g - 1);
                if (lastAmt > 2) {
                    moistCounts.put(4, lastAmt);
                }
                if (wordCounts.size() < 3) {
                    break elongate;
                }
                // take care of the interior letters
                double factor = 3 / (double) g; // ratio of moist interior length to word length
                for (Map.Entry<Integer, Integer> wc : wordCounts.entrySet()) {
                    if (wc.getValue() > 2) {
                        moistCounts.put((int) Math.round(wc.getKey() * factor), wc.getValue());
                    }
                }
            }

            // 7. replace the word with "moist", keeping original capitalization, defaulting to lower
            List<Character> word = chars.subList(i, t);
            List<Boolean> casing = word.stream().map(Character::isUpperCase).collect(Collectors.toList());

            word.clear();
            for (int f = 0; f < 5; f++) {

                // add e to moist letter index f to account for letter spans
                for (int e = 0; e < moistCounts.get(f); e++) {
                    int ef = e + f;
                    if (ef < casing.size()) {
                        word.add(casing.get(ef) ? Character.toUpperCase(MOIST[f]) : MOIST[f]);
                    }
                    else {
                        word.add(MOIST[f]);
                    }
                }
            }
            word.addAll(suffix);
        }
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private boolean isCharWordPart(char ch) {
        return Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_';
    }
}
