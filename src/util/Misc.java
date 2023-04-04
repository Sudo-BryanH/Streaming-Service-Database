package util;

import model.Artist;

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
}
