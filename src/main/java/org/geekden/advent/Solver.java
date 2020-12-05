package org.geekden.advent;

import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Solver implements Comparable<Solver> {
  protected static final Logger LOGGER = LoggerFactory.getLogger(Solver.class);

  protected final int year;
  protected final int day;

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

  @Override
  public int compareTo(Solver o) {
    return Comparator.comparing(Solver::year).thenComparing(Solver::day).compare(this, o);
  }

  public abstract String solvePartOne(Stream<String> input);
  public abstract String solvePartTwo(Stream<String> input);
}
