package dk.ablok.aoc2021;

import dk.ablok.aoc.AocDay;
import dk.ablok.aoc.AocPuzzle;
import dk.ablok.aoc.exceptions.AocLoadException;
import dk.ablok.aoc.exceptions.AocSolveException;
import dk.ablok.aoc.io.AocInput;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AocDay(year = 2021, day = 19)
public class AdventOfCode2021Day19 implements AocPuzzle {

    private static final Pattern ID_REGEX = Pattern.compile("^--- scanner (?<id>\\d+) ---$");
    private static final Pattern BEACON_REGEX = Pattern.compile("^(?<x>[-\\d]+),(?<y>[-\\d]+),(?<z>[-\\d]+)$");

    private final Set<Vector> fullMap = new HashSet<>();
    private final Map<Integer, Set<Vector>> scanners = new HashMap<>();
    private final Set<Vector> scannerPositions = new HashSet<>();

    @Override
    public void load() throws AocLoadException {
        var aocInput = new AocInput(2021, 19);
        for (var scanner : aocInput.readInputAsListSeparateByEmptyLine()) {

            Matcher idMatcher = ID_REGEX.matcher(scanner.getFirst());
            if (!idMatcher.find()) {
                throw new AocLoadException("Error parsing scanner ID");
            }

            Set<Vector> beacons = new HashSet<>();
            for (int i = 1; i < scanner.size(); i++) {
                Matcher beaconMatcher = BEACON_REGEX.matcher(scanner.get(i));
                if (!beaconMatcher.find()) {
                    throw new AocLoadException("Error parsing beacon");
                }
                beacons.add(new Vector(
                        Integer.parseInt(beaconMatcher.group("x")),
                        Integer.parseInt(beaconMatcher.group("y")),
                        Integer.parseInt(beaconMatcher.group("z"))));
            }

            scanners.put(Integer.parseInt(idMatcher.group("id")), beacons);
        }
    }

    @Override
    public String part1() throws AocSolveException {
        // Use scanner 0 as the origin. Add all its beacons to the completed map. Remove it from the map of scanners.
        fullMap.addAll(scanners.get(0));
        scanners.remove(0);

        // While there are still unplaced scanners
        while (!scanners.isEmpty()) {
            // For each scanner, determine how many distances it has in common with the fullMap
            Map<Integer, Long> distancesInCommon = calculateDistancesInCommon();

            // Pick the one with most overlap
            Integer maxEntry = distancesInCommon.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow()
                    .getKey();

            // Calculate the diff vectors between all beacons in the fullMap and the chosen scanner
            var allDiffs = calculateDiffs(fullMap);
            Set<Vector> diffs = calculateDiffs(scanners.get(maxEntry));

            // Determine the rotation that gives the most overlaps between the two sets of diffs
            int bestRotation = calculateBestRotation(allDiffs, diffs);

            // Determine the translation vector that brings the most beacons to overlap
            var rotatedBeacons = VectorRotator.rotate(scanners.get(maxEntry), bestRotation);
            var translationVector = calculateTranslationVector(rotatedBeacons);
            scannerPositions.add(translationVector);

            // Add all the shifted beacons to the map
            for (Vector rotatedBeacon : rotatedBeacons) {
                fullMap.add(rotatedBeacon.add(translationVector));
            }

            // Remove that scanner from the map of scanners
            scanners.remove(maxEntry);
            System.out.println();
        }

        // Count the unique beacons in the fullMap
        return Integer.toString(fullMap.size());
    }

    @Override
    public String part2() throws AocSolveException {
        int bestDistance = 0;
        for (Vector a : scannerPositions) {
            for (Vector b : scannerPositions) {
                int distance = Vector.diff(a, b).manhattanDistance();
                if (distance > bestDistance) {
                    bestDistance = distance;
                }
            }
        }
        return Integer.toString(bestDistance);
    }

    private Map<Integer, Long> calculateDistancesInCommon() {
        var allDistances = calculateDistances(fullMap);
        Map<Integer, Long> distancesInCommon = new HashMap<>();
        for (Map.Entry<Integer, Set<Vector>> scanner : scanners.entrySet()) {
            distancesInCommon.put(scanner.getKey(),
                    calculateDistances(scanner.getValue()).stream()
                            .filter(allDistances::contains)
                            .count());
        }
        return distancesInCommon;
    }

    private Set<Double> calculateDistances(Set<Vector> beacons) {
        // Calculate pairwise distances for all beacons
        Set<Double> distances = new HashSet<>();
        for (Vector a : beacons) {
            for (Vector b : beacons) {
                if (a != b) {
                    distances.add(Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2)));
                }
            }
        }
        return distances;
    }

    private Set<Vector> calculateDiffs(Set<Vector> beacons) {
        Set<Vector> diffs = new HashSet<>();
        for (Vector a : beacons) {
            for (Vector b : beacons) {
                if (a != b) {
                    diffs.add(Vector.diff(a, b));
                }
            }
        }
        return diffs;
    }

    private int calculateBestRotation(Set<Vector> allDiffs, Set<Vector> diffs) {
        // Rotate the diffs to maximize overlap of the inter-beacon diffs
        int bestRotation = 0;
        long bestOverlaps = 0;
        for (int r = 0; r < 24; r++) {
            var rotatedDiffs = VectorRotator.rotate(diffs, r);
            var overlaps = rotatedDiffs.stream()
                    .filter(allDiffs::contains)
                    .count();
            if (overlaps > bestOverlaps) {
                bestRotation = r;
                bestOverlaps = overlaps;
            }
        }
        return bestRotation;
    }

    private Vector calculateTranslationVector(Set<Vector> beacons) {
        // Calculate all diffs between the rotated beacons and the ones in the fullMap
        List<Vector> translationVectors = new ArrayList<>();
        for (Vector rotatedBeacon : beacons) {
            for (Vector mapBeacon : fullMap) {
                translationVectors.add(Vector.diff(rotatedBeacon, mapBeacon));
            }
        }

        // Find the translation vector that appears most often
        var translationVectorFrequency = translationVectors.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return translationVectorFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    record Vector(int x, int y, int z) {
        public static Vector diff(Vector a, Vector b) {
            return new Vector(b.x - a.x, b.y - a.y, b.z - a.z);
        }

        public Vector add(Vector a) {
            return new Vector(x + a.x, y + a.y, z + a.z);
        }

        public int manhattanDistance() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }
    }

    public static class VectorRotator {

        private static final List<int[]> ROTATIONS = generateRotations();

        private static Set<Vector> transform(Set<Vector> beacons, int[] rotation, int[] signs) {
            return beacons.stream()
                    .map(b -> {
                        int[] coords = {b.x(), b.y(), b.z()};

                        int newX = signs[0] * coords[rotation[0]];
                        int newY = signs[1] * coords[rotation[1]];
                        int newZ = signs[2] * coords[rotation[2]];

                        return new Vector(newX, newY, newZ);
                    })
                    .collect(Collectors.toSet());
        }


        private static List<int[]> generateRotations() {
            List<int[]> rotations = new ArrayList<>();

            // 24 orientations: 6 permutations × 4 sign combinations per permutation
            // We enumerate all valid right-handed coordinate systems

            int[][] permutations = {
                    {0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0}
            };

            for (int[] perm : permutations) {
                // Determine if permutation is even or odd (for right-hand rule)
                int parity = getPermutationParity(perm);

                // 4 sign combinations that preserve right-handedness
                int[][] signCombos = {
                        {1, 1, 1}, {1, -1, -1}, {-1, 1, -1}, {-1, -1, 1}
                };

                for (int[] signs : signCombos) {
                    // Adjust signs based on permutation parity
                    int[] adjustedSigns = adjustSigns(signs, parity);
                    rotations.add(concatenate(perm, adjustedSigns));
                }
            }

            return rotations;
        }

        private static int getPermutationParity(int[] perm) {
            // Count inversions to determine even(0) or odd(1) permutation
            int inversions = 0;
            for (int i = 0; i < perm.length; i++) {
                for (int j = i + 1; j < perm.length; j++) {
                    if (perm[i] > perm[j]) inversions++;
                }
            }
            return inversions % 2;
        }

        private static int[] adjustSigns(int[] signs, int parity) {
            // For odd permutations, flip one sign to maintain right-handed system
            if (parity == 1) {
                signs[2] = -signs[2];
            }
            return signs;
        }

        private static int[] concatenate(int[] perm, int[] signs) {
            int[] result = new int[6];
            System.arraycopy(perm, 0, result, 0, 3);
            System.arraycopy(signs, 0, result, 3, 3);
            return result;
        }

        public static Set<Vector> rotate(Set<Vector> beacons, int rotationIndex) {
            int[] rot = ROTATIONS.get(rotationIndex);
            int[] perm = Arrays.copyOfRange(rot, 0, 3);
            int[] signs = Arrays.copyOfRange(rot, 3, 6);
            return transform(beacons, perm, signs);
        }
    }
}
