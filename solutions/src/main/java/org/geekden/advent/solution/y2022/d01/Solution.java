package org.geekden.advent.solution.y2022.d01;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

import one.util.streamex.StreamEx;

public class Solution extends Solver {
  private final AtomicInteger currentElfId = new AtomicInteger(1);

  private record Elf(int id, long calories) {}

  public Solution() {
    super(2022, 1);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    currentElfId.set(1);
    Elf elfWithMostCalories = StreamEx.of(input)
        .groupRuns((a, b) -> !b.isEmpty())
        .map(this::fromListOfString)
        .maxBy(Elf::calories)
        .orElse(new Elf(0, 0));

    return String.valueOf(elfWithMostCalories.calories());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    currentElfId.set(1);
    long totalCaloriesOfTopThreeElves = StreamEx.of(input)
        .groupRuns((a, b) -> !b.isEmpty())
        .map(this::fromListOfString)
        .reverseSorted(Comparator.comparingLong(Elf::calories))
        .limit(3)
        .mapToLong(Elf::calories)
        .sum();

    return String.valueOf(totalCaloriesOfTopThreeElves);
  }

  private Elf fromListOfString(List<String> items) {
    long totalCalories = items.stream()
        .filter(Predicate.not(String::isEmpty))
        .mapToLong(Long::parseLong)
        .reduce(Long::sum)
        .orElse(0l);

    return new Elf(currentElfId.getAndIncrement(), totalCalories);
  }
}
