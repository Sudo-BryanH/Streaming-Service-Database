package util;

import model.Artist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Misc {
    public static boolean intToBool(int value) {
        return value >= 1;
    }

    public static String secondsToFormatted(int seconds) {
        int minutes = seconds / 60;
        int remainder = seconds % 60;

        return minutes + ":" + remainder + ((remainder < 10) ? "0" : "");
    }

    public static String artistsToString(List<Artist> artists) {
        StringBuilder result = new StringBuilder();

        for (Artist artist : artists) {
            result.append(artist.name).append(", ");
        }
        if (result.length() != 0) {
            result.delete(result.length() - 2, result.length());
        }

        return result.toString();
    }

    public static String slashDateToDash (String slashDate) {
        DateTimeFormatter slashDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(slashDate, slashDateFormatter);

        DateTimeFormatter dashDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dashDateFormatter);
    }

    public static List<String> stringCombinations (String input) {
        String[] words = input.split("\\s+");
        List<String> combinations = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j <= words.length; j++) {
                combinations.add(String.join(" ", Arrays.copyOfRange(words, i, j)));
            }
        }

        return combinations;
    }
}
