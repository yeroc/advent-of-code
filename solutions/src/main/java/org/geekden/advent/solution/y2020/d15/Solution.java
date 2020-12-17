package org.geekden.advent.solution.y2020.d15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

/**
 * On my aging mid-2012 laptop Part II runs in a little over ~10 seconds which is
 * unsatisfying.  See alternate implementation for a slightly optimized version.
 * I've kept this one as it uses core Java libraries and the alternate implementation
 * uses the exact same algorithm just with optimized Map implementation from the
 * fastutil library.
 *
 * @see Solution2
 */
public class Solution extends Solver {

  public Solution() {
    super(2020, 15, "Rambunctious Recitation");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    Game game = new Game(parseInput(input));
    return String.valueOf(game.playTo(2020));
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    Game game = new Game(parseInput(input));
    return String.valueOf(game.playTo(30_000_000));
  }

  List<Integer> parseInput(Stream<String> input) {
    return input
        .flatMap(l -> Stream.of(l.split(",")))
        .map(Integer::valueOf)
        .collect(Collectors.toList());
  }

  static class Game {
    // value -> turn
    Map<Integer, Integer> spokenNumbersToTurns = new HashMap<>(30_000_000);
    int lastTurn = 0;
    int lastNumber;

    Game(List<Integer> initialState) {
      for (Integer number : initialState) {
        recordTurn(number);
      }
    }

    public long playTo(int turns) {
      while (lastTurn < turns) {
        takeTurn();
      }
      return lastNumber;
    }

    private void takeTurn() {
      int currentNumber;
      Integer lastTurnSpoken = spokenNumbersToTurns.get(lastNumber);
      if (lastTurnSpoken == null) {
        currentNumber = 0;
      } else {
        currentNumber = lastTurn - lastTurnSpoken;
      }
      recordTurn(currentNumber);
    }

    private void recordTurn(int number) {
      int turn = lastTurn + 1;
      if (turn > 1) {
        spokenNumbersToTurns.put(lastNumber, lastTurn);
      }
      lastNumber = number;
      lastTurn = turn;
    }
  }
}
