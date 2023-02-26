# Advent of Code solutions in Java
Author: Anders Birk Sørensen
Email: anders@ablok.dk

This repository contains my solutions for AoC 2019, 2021 and 2022 written in Java. My solutions for 2021 were done in LabVIEW and can be found here.

All solutions can be compiled and run using Java 17. 

## Running
All solutions can called by running the test classes in src/dk/ablok/aoc/test/.

For the IntCode puzzles (2019) in particular, the test classes will disable visualizations for speed. To run them with visualizations, toggle the call to disableDisplay() in assertIntcodePuzzle(). 

## Structure
The code for each puzzle solution is kept in a single file except for library classes. The puzzles generally extend a common puzzle solution class 

## Notes