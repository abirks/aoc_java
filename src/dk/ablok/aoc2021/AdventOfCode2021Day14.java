package dk.ablok.aoc2021;

import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AdventOfCode2021Day14 implements AocPuzzle {

    private final Map<String, String> patterns = new HashMap<>();
    private Map<String, AtomicLong> pairs = new HashMap<>();
    private String template;

    @Override
    public void load(String filename) throws AocLoadException {
        File file = new File(filename);
        //TODO Use utils class
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            // Read template
            template = br.readLine();
            br.readLine();

            // Read patterns
            String inline;
            while ((inline = br.readLine()) != null) {
                String[] parts = inline.split(" -> ");
                patterns.put(parts[0], parts[1]);
            }

            for (String k : patterns.keySet()) {
                pairs.put(k, new AtomicLong(0L));
            }

            for (int i = 1; i < template.length(); i++) {
                pairs.get(template.substring(i - 1, i + 1)).incrementAndGet();
            }
        } catch (IOException e) {
            throw new AocLoadException(e);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        for (int n = 0; n < 10; n++) {
            doStep();
        }

        return Long.toString(score(template, pairs));
    }

    @Override
    public String part2() throws AocSolveException {
        for (int n = 10; n < 40; n++) {
            doStep();
        }
        return Long.toString(score(template, pairs));
    }

    private void doStep() {
        Map<String, AtomicLong> newPairs = new HashMap<>();
        for (String k : patterns.keySet()) {
            newPairs.put(k, new AtomicLong(0L));
        }

        for (Map.Entry<String, AtomicLong> e : pairs.entrySet()) {
            newPairs.get(e.getKey().charAt(0) + "" + patterns.get(e.getKey())).addAndGet(e.getValue().longValue());
            newPairs.get(patterns.get(e.getKey()) + "" + e.getKey().charAt(1)).addAndGet(e.getValue().longValue());
        }

        pairs = newPairs;
    }

    private long score(String template, Map<String, AtomicLong> map) {
        Map<String, AtomicLong> count = new HashMap<>();

        for (Map.Entry<String, AtomicLong> e : map.entrySet()) {
            if (count.containsKey(e.getKey().substring(0, 1))) {
                count.get(e.getKey().substring(0, 1)).addAndGet(e.getValue().longValue());
            } else {
                count.put(e.getKey().substring(0, 1), new AtomicLong(e.getValue().longValue()));
            }
        }
        count.get(template.substring(template.length() - 1)).incrementAndGet();

        long min = count.values().stream().mapToLong(AtomicLong::longValue).min().orElseThrow();
        long max = count.values().stream().mapToLong(AtomicLong::longValue).max().orElseThrow();
        return max - min;
    }
}
