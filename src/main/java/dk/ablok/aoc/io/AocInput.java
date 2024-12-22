package dk.ablok.aoc.io;

import dk.ablok.aoc.exceptions.AocLoadException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class AocInput {
    private final int year;
    private final int day;
    private final Properties properties;

    public AocInput(int year, int day) throws AocLoadException {
        this.year = year;
        this.day = day;
        this.properties = loadProperties();
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

    public String readAll() throws AocLoadException {
        try {
            return Files.readString(getFile());
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

    public List<Long> readSpaceSeparatedLongList() throws AocLoadException {
        return Arrays.stream(readFirstLine().split(" ")).map(Long::parseLong).toList();
    }

    public List<Integer> readNewlineSeparatedIntegerList() throws AocLoadException {
        return readInputAsList().stream().map(Integer::parseInt).toList();
    }

    public List<Long> readNewlineSeparatedLongList() throws AocLoadException {
        return readInputAsList().stream().map(Long::parseLong).toList();
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

    public Path getFile() throws AocLoadException {
        String inputUrl = String.format((String) properties.get("inputUrlFormat"), year, day);
        String localPath = String.format((String) properties.get("localPathFormat"), year, day);

        try {
            Path path = Path.of(localPath);
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                String content = downloadInput(inputUrl);
                saveToFile(content, localPath);
            }
            return path;
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    private String downloadInput(String url) throws AocLoadException {
        HttpURLConnection connection = getConnection(url);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            throw new AocLoadException("Exception while reading input stream from AoC website", e);
        }

    }

    private HttpURLConnection getConnection(String url) throws AocLoadException {
        String sessionCookie = (String) properties.get("sessionCookie");
        if (sessionCookie == null || sessionCookie.isEmpty()) {
            throw new AocLoadException("SessionCookie is not set in config.properties");
        }

        try {
            URL resourceUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) resourceUrl.openConnection();

            connection.setRequestProperty("Cookie", "session=" + sessionCookie);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new AocLoadException("Failed to fetch input: HTTP " + responseCode);
            }

            return connection;
        } catch (IOException e) {
            throw new AocLoadException("Exception while connecting to AoC website", e);
        }
    }

    private Properties loadProperties() throws AocLoadException {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("config.properties not found in resources");
            }
            prop.load(input);
            return prop;
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    private void saveToFile(String content, String filePath) throws IOException {
        Files.writeString(Path.of(filePath), content, StandardOpenOption.CREATE);
        System.out.printf("Downloaded input for day %d-%d%n", year, day);
    }
}
