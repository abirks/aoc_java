package dk.ablok.aoc.io;

import dk.ablok.aoc.exceptions.AocLoadException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class InputUtils {
    private InputUtils() {
    }

    public static List<String> readInputAsList(String filename) throws AocLoadException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.toList();
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    public static List<List<String>> readInputAsListSeparateByEmptyLine(String filename) throws AocLoadException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Iterator<String> iter = stream.iterator();

            List<List<String>> output = new ArrayList<>();
            List<String> list = new ArrayList<>();

            while (iter.hasNext()) {
                String line = iter.next();

                if (line.isEmpty()) {
                    output.add(list);
                    list = new ArrayList<>();
                } else {
                    list.add(line);
                }
            }

            // Add last set as well
            output.add(list);

            return output;
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    public static String readFirstLine(String filename) throws AocLoadException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.findFirst().orElseThrow();
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    public static List<Integer> readCommaSeparatedIntegerList(String filename) throws AocLoadException {
        return Arrays.stream(readFirstLine(filename).split(",")).map(Integer::parseInt).toList();
    }

    public static List<Long> readCommaSeparatedLongList(String filename) throws AocLoadException {
        return Arrays.stream(readFirstLine(filename).split(",")).map(Long::parseLong).toList();
    }

    public static List<Integer> readNewlineSeparatedIntegerList(String filename) throws AocLoadException {
        return readInputAsList(filename).stream().map(Integer::parseInt).toList();
    }

    public static char[][] read2dArray(String filename) throws AocLoadException {
        return read2dArray(filename, (char) 0);
    }

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

    public static char[] read1dArray(String filename) throws AocLoadException {
        byte[] bytes = readFirstLine(filename).getBytes(StandardCharsets.UTF_8);
        char[] output = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            output[i] = (char) bytes[i];
        }
        return output;
    }
}
