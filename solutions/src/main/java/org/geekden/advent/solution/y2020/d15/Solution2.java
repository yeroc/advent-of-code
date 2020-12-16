package org.geekden.advent.solution.y2020.d15;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

import com.google.common.base.Stopwatch;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * On my aging mid-2012-era laptop this is roughly twice is fast as my
 * other solution.  The only difference is this one uses an alternate
 * Map implementation provided by the fastutil library to avoid Integer
 * Object references reducing both overall memory usage and removing the
 * need for the JVM to auto-box between primitive ints and Integers.
 * Runtime is ~5 seconds on my laptop.
 */
public class Solution2 extends Solver {

  public Solution2() {
    super(2020, 15);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    Game game = new Game(parseInput(input));
    return String.valueOf(game.playTo(2020));
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    Stopwatch timer = Stopwatch.createStarted();
    Game game = new Game(parseInput(input));
    long result = game.playTo(30_000_000);
    timer.stop();
    LOGGER.info("Elapsed: {}", timer.elapsed());
    return String.valueOf(result);
  }

  List<Integer> parseInput(Stream<String> input) {
    return input
        .flatMap(l -> Stream.of(l.split(",")))
        .map(l -> Integer.valueOf(l))
        .collect(Collectors.toList());
  }

  static class Game {
    // value -> turn
    Int2IntMap spokenNumbersToTurns = new Int2IntOpenHashMap(30_000_000);
    int lastTurn = 0;
    int lastNumber;

    Game(List<Integer> initialState) {
      spokenNumbersToTurns.defaultReturnValue(-1);
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
      int lastTurnSpoken = spokenNumbersToTurns.get(lastNumber);
      if (lastTurnSpoken == -1) {
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
