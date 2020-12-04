package org.geekden.advent;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Day implements Comparable<Day>, PartOneSolver {
  protected static final Logger LOGGER = LoggerFactory.getLogger(Day.class);

  protected final int year;
  protected final int day;

  protected Day(int year, int day) {
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
  public int compareTo(Day o) {
    return Comparator.comparing(Day::year).thenComparing(Day::day).compare(this, o);
  }
}
