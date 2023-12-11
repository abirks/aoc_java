package dk.ablok.aoc.io;

import dk.ablok.aoc.exceptions.AocLoadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class AocInput {
    private final int year;
    private final int day;

    public AocInput(int year, int day) {
        this.year = year;
        this.day = day;
    }

    public List<String> readInputAsList() throws AocLoadException {
        try (Stream<String> stream = Files.lines(getFile())) {
            return stream.toList();
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    public List<List<String>> readInputAsListSeparateByEmptyLine() throws AocLoadException {
        try (Stream<String> stream = Files.lines(getFile())) {
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

    public String readFirstLine() throws AocLoadException {
        try (Stream<String> stream = Files.lines(getFile())) {
            return stream.findFirst().orElseThrow();
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    public List<Integer> readCommaSeparatedIntegerList() throws AocLoadException {
        return Arrays.stream(readFirstLine().split(",")).map(Integer::parseInt).toList();
    }

    public List<Long> readCommaSeparatedLongList() throws AocLoadException {
        return Arrays.stream(readFirstLine().split(",")).map(Long::parseLong).toList();
    }

    public List<Integer> readNewlineSeparatedIntegerList() throws AocLoadException {
        return readInputAsList().stream().map(Integer::parseInt).toList();
    }

    public char[][] read2dArray() throws AocLoadException {
        return read2dArray((char) 0);
    }

    public char[][] read2dArray(char defaultValue) throws AocLoadException {
        List<String> lines = readInputAsList();

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

    public char[] read1dArray() throws AocLoadException {
        byte[] bytes = readFirstLine().getBytes(StandardCharsets.UTF_8);
        char[] output = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            output[i] = (char) bytes[i];
        }
        return output;
    }

    public Path getFile() {
        String inputUrl = String.format("https://adventofcode.com/%d/day/%d/input", year, day);
        String localPath = String.format("input/aoc%04d/input%02d.txt", year, day);

        try {
            if (!fileExists(localPath)) {
                String content = downloadInput(inputUrl);
                saveToFile(content, localPath);
            }
            return Path.of(localPath);
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    private boolean fileExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }

    private String downloadInput(String url) throws IOException {
        URL resourceUrl = new URL(url);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private void saveToFile(String content, String filePath) throws IOException {
        Files.writeString(Path.of(filePath), content, StandardOpenOption.CREATE);
        System.out.printf("Downloaded input for day %d-%d%n", year, day);
    }
}
