package org.geekden.advent.solution.y2022.d02;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

public class Solution extends Solver {

  enum State {
    WIN(6), LOSE(0), DRAW(3);

    int score;

    State(int score) {
      this.score = score;
    }

    public static State fromChar(char input) {
      switch (input) {
        case 'X':
          return LOSE;
        case 'Y':
          return DRAW;
        case 'Z':
          return WIN;
        default:
          throw new IllegalArgumentException("Unknown input " + input);
      }
    }
  }

  enum Shape {
    ROCK(1), PAPER(2), SCISSORS(3);

    int score;

    Shape(int score) {
      this.score = score;
    }

    public State versus(Shape other) {
      if (this == other) {
        return State.DRAW;
      } else if (this == ROCK && other == SCISSORS) {
        return State.WIN;
      } else if (this == SCISSORS && other == PAPER) {
        return State.WIN;
      } else if (this == PAPER && other == ROCK) {
        return State.WIN;
      } else {
        return State.LOSE;
      }
    }

    public Shape solve(State desiredState) {
      if (desiredState == State.WIN) {
        desiredState = State.LOSE;
      } else if (desiredState == State.LOSE) {
        desiredState = State.WIN;
      }
      for (Shape other : Shape.values()) {
        if (versus(other) == desiredState) {
          return other;
        }
      }
      throw new IllegalStateException("Unsolvable for " + this + " with desired state of " + desiredState);
    }

    public static Shape fromChar(char input) {
      switch (input) {
        case 'A':
        case 'X':
          return ROCK;
        case 'B':
        case 'Y':
          return PAPER;
        case 'C':
        case 'Z':
          return SCISSORS;
        default:
          throw new IllegalArgumentException("Unknown input " + input);
      }
    }
  }

  public Solution() {
    super(2022, 2);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    AtomicInteger totalScore = new AtomicInteger(0);
    input.forEach(l -> {
      String[] pieces = l.split(" ");
      Shape opponent = Shape.fromChar(pieces[0].charAt(0));
      Shape me = Shape.fromChar(pieces[1].charAt(0));
      totalScore.addAndGet(me.score);
      totalScore.addAndGet(me.versus(opponent).score);
    });
    return String.valueOf(totalScore.get());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    AtomicInteger totalScore = new AtomicInteger(0);
    input.forEach(l -> {
      String[] pieces = l.split(" ");
      Shape opponent = Shape.fromChar(pieces[0].charAt(0));
      State desiredState = State.fromChar(pieces[1].charAt(0));
      Shape me = opponent.solve(desiredState);
      totalScore.addAndGet(me.score);
      totalScore.addAndGet(me.versus(opponent).score);
    });
    return String.valueOf(totalScore.get());
  }
  
}
