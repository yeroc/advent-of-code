package org.geekden.advent.solution.y2020.d17;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2020, 17);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    return String.valueOf(simulate(parseInput(input, false)));
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    return String.valueOf(simulate(parseInput(input, true)));
  }

  private int simulate(PocketDimension4d pocketDimension) {
    int cycles = 6;
    for (int i = 0; i < cycles; i++) {
      pocketDimension = pocketDimension.cycle();
    }
    return pocketDimension.totalActiveCubes;
  }

  PocketDimension4d parseInput(Stream<String> input, boolean hyper) {
    List<String> lines = input.collect(Collectors.toList());
    PocketDimension4d pd = new PocketDimension4d(hyper);
    int x = 0, y = 0, z = 0, w = 0;
    for (String string : lines) {
      x++;
      y = 0;
      for (char c : string.toCharArray()) {
        y++;
        if (c == '#') {
          pd.addActiveCube(x, y, z, w);
        }
      }
    }
    return pd;
   }

  static class Range {
    int min;
    int max;

    void extend(int val) {
      if (val < min) {
        min = val;
      }

      if (val > max) {
        max = val;
      }
    }
  }

  static class PocketDimension4d {
    // x -> y -> z -> w -> state
    Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>> cube = new HashMap<>();
    Range xRange = new Range();
    Range yRange = new Range();
    Range zRange = new Range();
    Range wRange = new Range();
    int totalActiveCubes = 0;
    final boolean hyper;

    public PocketDimension4d(boolean hyper) {
      this.hyper = hyper;
    }

    public void addActiveCube(int x, int y, int z, int w) {
      Map<Integer, Map<Integer, Map<Integer, Boolean>>> xMap = cube.computeIfAbsent(x, a -> new HashMap<>());
      Map<Integer, Map<Integer, Boolean>> yMap = xMap.computeIfAbsent(y, a -> new HashMap<>());
      Map<Integer, Boolean> zMap = yMap.computeIfAbsent(z, a -> new HashMap<>());
      zMap.put(w, Boolean.TRUE);
      xRange.extend(x);
      yRange.extend(y);
      zRange.extend(z);
      wRange.extend(w);
      totalActiveCubes++;
    }

    public boolean getCubeState(int x, int y, int z, int w) {
      Map<Integer, Map<Integer, Map<Integer, Boolean>>> xMap = cube.getOrDefault(x, new HashMap<>());
      Map<Integer, Map<Integer, Boolean>> yMap = xMap.getOrDefault(y, new HashMap<>());
      Map<Integer, Boolean> zMap = yMap.getOrDefault(z, new HashMap<>());
      return zMap.getOrDefault(w, false);
    }

    public int countNeighbours(int x, int y, int z, int w) {
      int activeCubes = 0;
      // if not hyper then constrain to 3d...
      int startW = hyper ? -1 : 0;
      int endW = hyper ? 1 : 0;

      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          for (int k = -1; k <= 1; k++) {
            for (int l = startW; l <= endW ; l++) {
              if (i == 0 && j == 0 && k == 0 && l == 0) continue;
              int checkX = x + i;
              int checkY = y + j;
              int checkZ = z + k;
              int checkW = w + l;
              if (getCubeState(checkX, checkY, checkZ, checkW)) {
                activeCubes++;
              }
            }
          }
        }
      }
      return activeCubes;
    }

    public PocketDimension4d cycle() {
      PocketDimension4d nextCycle = new PocketDimension4d(hyper);
      for (int x = xRange.min - 1; x <= xRange.max + 1; x++) {
        for (int y = yRange.min - 1; y <= yRange.max + 1; y++) {
          for (int z = zRange.min - 1; z <= zRange.max + 1; z++) {
            for (int w = wRange.min - 1; w <= wRange.max + 1; w++) {
              int neighbours = countNeighbours(x, y, z, w);
              boolean state = getCubeState(x, y, z, w);
              if (state && neighbours > 1 && neighbours < 4 ||
                  !state && neighbours == 3) {
                nextCycle.addActiveCube(x, y, z, w);
              }
            }
          }
        }
      }
      return nextCycle;
    }
  }
}
