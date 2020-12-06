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
  private final Set<Solver> solutions;
  private final Path inputBasePath;

  Driver(Set<Solver> solutions, Path inputBasePath) throws Exception {
    this.solutions = solutions;
    this.inputBasePath = inputBasePath;
  }

  void execute() throws Exception {
    for (Solver solution : solutions) {
      Path inputFile = inputBasePath.resolve(format("%d/%02d/input.txt", solution.year(), solution.day()));

      if (Files.notExists(inputFile)) {
        System.err.printf("Input file %s not found for %d, day %d.\n", inputFile, solution.year(), solution.day());
        continue;
      }

      String partOneSolution = solve(solution::solvePartOne, inputFile);
      printSolution(solution, partOneSolution, "I");

      String partTwoSolution = solve(solution::solvePartTwo, inputFile);
      printSolution(solution, partTwoSolution, "II");
    }
  }

  private static String solve(Function<Stream<String>, String> fn, Path inputFile) throws IOException {
    try (Stream<String> input = Files.lines(inputFile, StandardCharsets.UTF_8)) {
      return fn.apply(input);
    }
  }

  private static void printSolution(Solver day, String solution, String part) {
    System.out.printf("Solution for %d, day %d, part %-2s: [%25s]\n",
        day.year(), day.day(), part, solution);
  }
}
