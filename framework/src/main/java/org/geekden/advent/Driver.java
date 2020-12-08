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
  private final boolean useSampleData;

  Driver(Set<Solver> solutions, Path inputBasePath, boolean useSampleData) throws Exception {
    this.solutions = solutions;
    this.inputBasePath = inputBasePath;
    this.useSampleData = useSampleData;
  }

  void execute() throws Exception {
    String inputFilename = useSampleData ? "sample-input.txt" : "input.txt";
    for (Solver solution : solutions) {
      Path inputFile = inputBasePath.resolve(format("%d/%02d/%s", solution.year(), solution.day(), inputFilename));

      if (Files.notExists(inputFile)) {
        System.err.printf("Input file %s not found for %d, day %d.\n", inputFile, solution.year(), solution.day());
        continue;
      }

      String part = "I";
      try {
        String partOneSolution = solve(solution::solvePartOne, inputFile);
        printSolution(solution, partOneSolution, part);
      } catch (Exception e) {
        printProblem(solution, e, part);
      }

      part = "II";
      try {
        String partTwoSolution = solve(solution::solvePartTwo, inputFile);
        printSolution(solution, partTwoSolution, part);
      } catch (Exception e) {
        printProblem(solution, e, part);
      }
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

  private static void printProblem(Solver day, Throwable t, String part) {
    System.err.printf("Problem (rather than solution) for %d, day %d, part %-2s: %s!\nTrace: ",
        day.year(), day.day(), part, t.getMessage());
    t.printStackTrace(System.err);
  }
}
