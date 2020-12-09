package org.geekden.advent.solution.y2020.d03;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {

  private static final int MAP_WIDTH = 31;
  private String map;

  public Solution() {
    super(2020, 3);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    map = input.collect(Collectors.joining());

    return String.valueOf(traverseMap(new Location(), new Slope(3, 1)));
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    map = input.collect(Collectors.joining());

    List<Slope> slopes = new ArrayList<>();
    slopes.add(new Slope(3, 1));
    slopes.add(new Slope(1, 1));
    slopes.add(new Slope(5, 1));
    slopes.add(new Slope(7, 1));
    slopes.add(new Slope(1, 2));

    return String.valueOf(slopes.stream()
      .mapToLong(s -> traverseMap(new Location(), s))
      .reduce(1, (a, b) -> a * b));
  }

  private Geology geologyAtLocation(Location location) {
    int idx = (location.x % MAP_WIDTH) + (location.y * MAP_WIDTH);
    LOGGER.debug("Calculated index is {} for {}.", idx, location);
    if (idx >= map.length()) {
      return null;
    } else {
      return Geology.byChar(map.charAt(idx));
    }
  }

  private int traverseMap(Location location, Slope slope) {
    int treeCount = 0;
    Geology geology;
    while ((geology = geologyAtLocation(location)) != null) {
      LOGGER.debug("Geology at {} is {}.", location, geology);
      if (geology == Geology.TREE) {
        treeCount++;
      }
      location.advanceBy(slope);
    }
    return treeCount;
  }

  enum Geology {
    TREE('#'), OPEN('.');

    private final char marker;

    Geology(char marker) {
      this.marker = marker;
    }

    public static Geology byChar(char marker) {
      for (Geology geology : Geology.values()) {
        if (marker == geology.marker) {
          return geology;
        }
      }
      throw new IllegalArgumentException(String.format("Unknown geology: %c!", marker));
    }
  }

  static class Location {
    int x = 0;
    int y = 0;

    public void advanceBy(Slope slope) {
      x = x + slope.dx;
      y = y + slope.dy;
    }
  }

  static class Slope {
    final int dx;
    final int dy;

    public Slope(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }
  }
}
