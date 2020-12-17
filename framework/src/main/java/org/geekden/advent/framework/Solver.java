package org.geekden.advent.framework;

import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Solver implements Comparable<Solver> {
  protected static final Logger LOGGER = LoggerFactory.getLogger(Solver.class);

  private final int year;
  private final int day;
  private final String name;

  protected Solver(int year, int day) {
    this(year, day, null);
  }

  protected Solver(int year, int day, String name) {
    this.year = year;
    this.day = day;
    this.name = name;
  }

  public int year() {
    return year;
  }

  public int day() {
    return day;
  }

  public String name() {
    return name != null ? "“" + name + "”" : "[" + this.getClass().getSimpleName() + "]";
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
