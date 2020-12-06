package org.geekden.advent.solution.y2020.d01;

import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2020, 1);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    int[] ledgerInts = input.mapToInt((s) -> Integer.valueOf(s)).toArray();

    for (int i = 0; i < ledgerInts.length; i++) {
      for (int j = 0; j < ledgerInts.length; j++) {
        if (i == j) continue;
        if (ledgerInts[i] + ledgerInts[j] == 2020) {
          return String.valueOf(ledgerInts[i] * ledgerInts[j]);
        }
      }
    }
    throw new IllegalStateException("No solution found!");
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    int[] ledgerInts = input.mapToInt((s) -> Integer.valueOf(s)).toArray();

    for (int i = 0; i < ledgerInts.length; i++) {
      for (int j = 0; j < ledgerInts.length; j++) {
        for (int k = 0; k < ledgerInts.length; k++) {
          if (i == j && j == k) continue;
          if (ledgerInts[i] + ledgerInts[j] + ledgerInts[k] == 2020) {
            return String.valueOf(ledgerInts[i] * ledgerInts[j] * ledgerInts[k]);
          }
        }
      }
    }
    throw new IllegalStateException("No solution found!");
  }
}
