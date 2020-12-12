package org.geekden.advent;

import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Solver implements Comparable<Solver> {
  protected static final Logger LOGGER = LoggerFactory.getLogger(Solver.class);

  private final int year;
  private final int day;

  protected Solver(int year, int day) {
    this.year = year;
    this.day = day;
  }

  public int year() {
    return year;
  }

  public int day() {
    return day;
  }

  private String getClassName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public int compareTo(Solver o) {
    return Comparator
        .comparingInt(Solver::year)
        .thenComparingInt(Solver::day)
        .thenComparing(Solver::getClassName)
        .compare(this, o);
  }

  public abstract String solvePartOne(Stream<String> input);
  public abstract String solvePartTwo(Stream<String> input);
}
