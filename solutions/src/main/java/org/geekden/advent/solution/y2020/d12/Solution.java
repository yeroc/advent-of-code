package org.geekden.advent.solution.y2020.d12;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

import com.google.common.base.MoreObjects;

public class Solution extends Solver {

  public Solution() {
    super(2020, 12);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    List<NavInstruction> instructions = parseInput(input);
    Position position = calculatePositionPartOne(instructions);
    return String.valueOf(position.manhattanDistance());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    List<NavInstruction> instructions = parseInput(input);
    Position position = calculatePositionPartTwo(instructions);
    return String.valueOf(position.manhattanDistance());
  }

  private Position calculatePositionPartOne(List<NavInstruction> instructions) {
    Position shipPosition = new Position(0, 0);
    int heading = 0; // east

    for (NavInstruction navInstruction : instructions) {
      LOGGER.debug("{}", navInstruction);
      Action action = navInstruction.action;
      if (action == Action.F) {
        action = Action.values()[heading / 90];
      }
      switch (action) {
      case N:
        shipPosition.north(navInstruction.amount);
        break;
      case S:
        shipPosition.south(navInstruction.amount);
        break;
      case E:
        shipPosition.east(navInstruction.amount);
        break;
      case W:
        shipPosition.west(navInstruction.amount);
        break;
      case L:
        heading = (heading + 360 - navInstruction.amount) % 360;
        break;
      case R:
        heading = (heading + navInstruction.amount) % 360;
        break;
      default:
        break;
      }
      LOGGER.debug("Ship: {}, {}", shipPosition, Action.values()[heading / 90]);
    }
    return shipPosition;
  }

  private Position calculatePositionPartTwo(List<NavInstruction> instructions) {

    Position waypointPosition = new Position(10, 1);
    Position shipPosition = new Position(0, 0);

    for (NavInstruction navInstruction : instructions) {
      LOGGER.debug("{}", navInstruction);
      Action action = navInstruction.action;
      switch (action) {
      case N:
        waypointPosition.north(navInstruction.amount);
        break;
      case S:
        waypointPosition.south(navInstruction.amount);
        break;
      case E:
        waypointPosition.east(navInstruction.amount);
        break;
      case W:
        waypointPosition.west(navInstruction.amount);
        break;
      case L:
      case R:
        int clockwiseQuarterTurns;
        if (action == Action.L) {
          clockwiseQuarterTurns = (360 - navInstruction.amount) / 90;
        } else {
          clockwiseQuarterTurns = navInstruction.amount / 90;
        }
        for (int i = 0; i < clockwiseQuarterTurns; i++) {
          waypointPosition.rotateQuarterTurn();
        }
        break;
      case F:
        shipPosition.multiplyVector(waypointPosition, navInstruction.amount);
      default:
        break;
      }
      LOGGER.debug("Ship: {}, Waypoint: {}", shipPosition, waypointPosition);
    }
    return shipPosition;
  }

  private List<NavInstruction> parseInput(Stream<String> input) {
    return input.map(l ->
      new NavInstruction(Action.valueOf(l.substring(0, 1)), Integer.parseInt(l.substring(1))))
    .collect(Collectors.toList());
  }

  static class NavInstruction {
    final Action action;
    final int amount;

    NavInstruction(Action action, int amount) {
      this.action = action;
      this.amount = amount;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(NavInstruction.class).add("action", action).add("amount", amount).toString();
    }
  }

  enum Action {
    E, S, W, N, L, F, R
  }

  static class Position {
    int x;
    int y;

    Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    void north(int amt) {
      y += amt;
    }

    void south(int amt) {
      y -= amt;
    }

    void east(int amt) {
      x += amt;
    }

    void west(int amt) {
      x -= amt;
    }

    void rotateQuarterTurn() {
      int x = this.y;
      int y = -this.x;
      this.x = x;
      this.y = y;
    }

    void multiplyVector(Position waypoint, int amt) {
      x += waypoint.x * amt;
      y += waypoint.y * amt;
    }

    int manhattanDistance() {
      return Math.abs(x) + Math.abs(y);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(Position.class).add("x", x).add("y", y).toString();
    }
  }
}
