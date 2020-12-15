package org.geekden.advent.solution.y2020.d15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

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
    Game game = new Game(parseInput(input));
    return String.valueOf(game.playTo(30_000_000));
  }

  List<Long> parseInput(Stream<String> input) {
    return input
        .flatMap(l -> Stream.of(l.split(",")))
        .map(l -> Long.valueOf(l))
        .collect(Collectors.toList());
  }


  static class Game {
    // value -> turn
    Map<Long, Long> spokenNumbersToTurns = new HashMap<>(30_000_000);
    long lastTurn = 0;
    long lastNumber;

    Game(List<Long> initialState) {
      for (Long number : initialState) {
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
      long currentNumber;
      if (!spokenNumbersToTurns.containsKey(lastNumber)) {
        currentNumber = 0l;
      } else {
        long lastTurnSpoken = spokenNumbersToTurns.get(lastNumber);
        currentNumber = lastTurn - lastTurnSpoken;
      }
      recordTurn(currentNumber);
    }

    private void recordTurn(long number) {
      long turn = lastTurn + 1;
      if (turn > 1) {
        spokenNumbersToTurns.put(lastNumber, lastTurn);
      }
      lastNumber = number;
      lastTurn = turn;
    }
  }
}
