package org.geekden.advent;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

class Driver {
  private final Set<Solver> days;
  private final Path inputBasePath;

  Driver(Set<Solver> days, Path inputBasePath) throws Exception {
    this.days = days;
    this.inputBasePath = inputBasePath;
  }

  void execute() throws Exception {
    for (Solver day : days) {
      Path inputFile = inputBasePath.resolve(format("%d/day%d/input.txt", day.year(), day.day()));

      if (Files.notExists(inputFile)) {
        System.err.printf("Input file %s not found for %d, day %d.\n", inputFile, day.year(), day.day());
        continue;
      }

      String partOneSolution = solve(day::solvePartOne, inputFile);
      printSolution(day, partOneSolution, "I");

      String partTwoSolution = solve(day::solvePartTwo, inputFile);
      printSolution(day, partTwoSolution, "II");
    }
  }

  private String solve(Function<Stream<String>, String> fn, Path inputFile) throws IOException {
    try (Stream<String> input = Files.lines(inputFile, StandardCharsets.UTF_8)) {
      return fn.apply(input);
    }
  }

  private void printSolution(Solver day, String solution, String part) {
    System.out.printf("Solution for %d, day %d, part %-2s: [%25s]\n",
        day.year(), day.day(), part, solution);
  }
}
