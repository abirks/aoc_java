# Advent of Code solutions in Java

Author: Anders Birk Sørensen
Email: anders@ablok.dk

This repository contains my solutions for Advent of Code written in Java. My solutions for 2021 were done in
LabVIEW and can be found [here](https://github.com/abirks/aoc20).

All solutions can be compiled and run using Java 25.

## Running

All solutions can called by running the test class in src/test/java/dk/ablok.

For the IntCode puzzles (2019) in particular, the test class will disable visualizations for speed. To run them with
visualizations, toggle the call to disableDisplay() in test2019().

## Structure

The code for each puzzle solution is kept in a single file except for shared utilities. The puzzles implement a common
interface to make execution more uniform.

The interface contains three methods:

* ```void load();```: Load the input and parses it into appropriate data structures
* ```String part1();```: Performs calculations to solve part 1 of the puzzle and returns the result as a String
* ```String part2();```: Performs calculations to solve part 2 of the puzzle and returns the result as a String

Additionally, puzzles implementing the ```AocPuzzleWithDisplay``` interface has an ```enableDisplay``` method to enable
or disable visualization for unit testing.

Each solution implementation is annotated with the ```AocDay``` annotation. This annotation is used by the test classes
for finding the implementation for each day.

## Refactoring projects for when I have time

* Fix all TODOs
* A common, general purpose 2D vector class with methods for adding, subtracting and rotating.
* Flood-fill algorithm with functional interfaces to calculate information along the way and predicate for stop
  condition
    * Refactor previous puzzles to use this for flood-fill and BFS
* Use LeastCommonMultiple class for previous years' puzzles that require it
