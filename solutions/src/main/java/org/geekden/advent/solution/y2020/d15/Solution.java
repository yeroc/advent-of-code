package org.geekden.advent.solution.y2020.d15;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

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
    // value -> turns
    Multimap<Long, Long> spokenNumbersToTurns = ArrayListMultimap.create(30_000_000, 3);
    long lastTurn = 0;
    long lastNumber;

    Game(List<Long> initialState) {
      for (Long number : initialState) {
        lastNumber = number;
        record(number, ++lastTurn);
      }
    }

    public long playTo(int turns) {
      while (lastTurn < turns) {
        takeTurn();
      }
      return lastNumber;
    }

    public void takeTurn() {
      Collection<Long> turnsForLastNumber = spokenNumbersToTurns.get(lastNumber);
      long currentNumber;
      if (turnsForLastNumber.size() == 1) {
        currentNumber = 0;
      } else {
        long prevTurn = 0l;
        long diffTurn = 0l;
        for (Long turn : turnsForLastNumber) {
          diffTurn = turn - prevTurn;
          prevTurn = turn;
        }
        currentNumber = diffTurn;
      }
      record(currentNumber, ++lastTurn);
      lastNumber = currentNumber;
    }

    private void record(long number, long turn) {
      spokenNumbersToTurns.put(number, turn);

      Collection<Long> turnsForLastNumber = spokenNumbersToTurns.get(lastNumber);
      if (turnsForLastNumber.size() > 2) {
        Iterator<Long> iterator = turnsForLastNumber.iterator();
        iterator.next();
        iterator.remove();
      }
    }
  }
}
