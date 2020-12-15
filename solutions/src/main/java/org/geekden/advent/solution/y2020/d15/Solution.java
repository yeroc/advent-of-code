package org.geekden.advent.solution.y2020.d15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

import com.google.common.base.Stopwatch;

public class Solution extends Solver {

  public Solution() {
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
    LOGGER.debug("Elapsed: {}", timer.elapsed());
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
