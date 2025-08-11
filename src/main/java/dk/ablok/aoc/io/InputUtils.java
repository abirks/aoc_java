package dk.ablok.aoc.io;

import dk.ablok.aoc.exceptions.AocLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Deprecated
public class InputUtils {
    private static final Pattern PATH_PATTERN = Pattern.compile("input/aoc(-?\\d+)/input(-?\\d+).txt$");

    private InputUtils() {
    }

    private static AocInput getInput(String filename) throws AocLoadException {
        var results = PATH_PATTERN.matcher(filename);

        if (!results.find()) {
            throw new AocLoadException("Filename does not match pattern!");
        }

        int year = Integer.parseInt(results.group(1));
        int day = Integer.parseInt(results.group(2));
        return new AocInput(year, day);
    }


    @Deprecated
    public static List<String> readInputAsList(String filename) throws AocLoadException {
        return getInput(filename).readInputAsList();
    }

    @Deprecated
    public static List<List<String>> readInputAsListSeparateByEmptyLine(String filename) throws AocLoadException {
        return getInput(filename).readInputAsListSeparateByEmptyLine();
    }

    @Deprecated
    public static String readFirstLine(String filename) throws AocLoadException {
        return getInput(filename).readFirstLine();
    }

    @Deprecated
    public static List<Integer> readCommaSeparatedIntegerList(String filename) throws AocLoadException {
        return Arrays.stream(readFirstLine(filename).split(",")).map(Integer::parseInt).toList();
    }

    @Deprecated
    public static List<Long> readCommaSeparatedLongList(String filename) throws AocLoadException {
        return Arrays.stream(readFirstLine(filename).split(",")).map(Long::parseLong).toList();
    }

    @Deprecated
    public static List<Integer> readNewlineSeparatedIntegerList(String filename) throws AocLoadException {
        return readInputAsList(filename).stream().map(Integer::parseInt).toList();
    }

    @Deprecated
    public static char[][] read2dArray(String filename) throws AocLoadException {
        return read2dArray(filename, (char) 0);
    }

    @Deprecated
    public static char[][] read2dArray(String filename, char defaultValue) throws AocLoadException {
        List<String> lines = readInputAsList(filename);

        int maxLineLength = lines.stream().mapToInt(String::length).max().orElseThrow();
        char[][] output = new char[lines.size()][maxLineLength];

        for (int y = 0; y < lines.size(); y++) {
            byte[] line = lines.get(y).getBytes(StandardCharsets.UTF_8);

            for (int x = 0; x < line.length; x++) {
                output[y][x] = (char) line[x];
            }

            // Pad lines that are shorter than the max length
            for (int x = line.length; x < maxLineLength; x++) {
                output[y][x] = defaultValue;
            }
        }

        return output;
    }

    @Deprecated
    public static char[] read1dArray(String filename) throws AocLoadException {
        byte[] bytes = readFirstLine(filename).getBytes(StandardCharsets.UTF_8);
        char[] output = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            output[i] = (char) bytes[i];
        }
        return output;
    }
}
