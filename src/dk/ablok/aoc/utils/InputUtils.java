package dk.ablok.aoc.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class InputUtils {
    private InputUtils() {}

    public static List<String> readInputAsList(String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.toList();
        }
    }

    public static List<List<String>> readInputAsListSeparateByEmptyLine(String filename) throws IOException {
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
        }
    }

    public static String readFirstLine(String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.findFirst().orElseThrow();
        }
    }

    public static List<Integer> convertStringListToIntegers(List<String> strings) {
        return strings.stream().map(Integer::parseInt).toList();
    }


    public static List<Integer> readIntegerList(String filename) throws IOException {
        return Arrays.stream(readFirstLine(filename).split(",")).map(Integer::parseInt).toList();
    }

    public static List<Long> readLongList(String filename) throws IOException {
        return Arrays.stream(readFirstLine(filename).split(",")).map(Long::parseLong).toList();
    }
}
