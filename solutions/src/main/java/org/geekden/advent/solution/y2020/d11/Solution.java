package org.geekden.advent.solution.y2020.d11;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {
  private int width;
  private int height;
  private String initialState;

  public Solution() {
    super(2020, 11);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    initialState = parseInput(input);
    height = initialState.length() / width;

    Room currentGeneration = new Room(width, height, true, initialState);
    int generation = 0;

//    LOGGER.info("{}: {}", generation, currentGeneration.state);
    for (generation = 1; generation < 1000; generation++) {
      Room nextGeneration = currentGeneration.nextGeneration();

//      LOGGER.info("{}: {}", generation, nextGeneration.state);

      if (currentGeneration.equals(nextGeneration)) {
        break;
      }
      currentGeneration = nextGeneration;
    }

    return String.valueOf(currentGeneration.occupiedSeats());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    initialState = parseInput(input);
    height = initialState.length() / width;

    Room currentGeneration = new Room(width, height, false, initialState);
    int generation = 0;

//    LOGGER.info("{}: {}", generation, currentGeneration.state);
    for (generation = 1; generation < 1000; generation++) {
      Room nextGeneration = currentGeneration.nextGeneration();

//      LOGGER.info("{}: {}", generation, nextGeneration.state);

      if (currentGeneration.equals(nextGeneration)) {
        break;
      }
      currentGeneration = nextGeneration;
    }

    return String.valueOf(currentGeneration.occupiedSeats());
  }


  private String parseInput(Stream<String> input) {
    return input.peek(l -> width = l.length()).collect(Collectors.joining());
  }

  enum State {
    FLOOR('.'), EMPTY_SEAT('L'), OCCUPIED_SEAT('#');

    private final char marker;

    State(char marker) {
      this.marker = marker;
    }

    public static State byChar(char marker) {
      for (State state : State.values()) {
        if (marker == state.marker) {
          return state;
        }
      }
      throw new IllegalArgumentException(String.format("Unknown state: %c!", marker));
    }
  }

  static class Room {
    private final int width;
    private final int height;
    private final String state;
    private final boolean useAdjacency;
    private final int occupancyThreshold;

    public Room(int width, int height, boolean useAdjacency, String state) {
      this.width = width;
      this.height = height;
      this.useAdjacency = useAdjacency;
      this.state = state;
      occupancyThreshold = useAdjacency ? 4 : 5;
    }

    public State stateAt(int x, int y) {
      int idx = (x % width) + (y * width);
      return State.byChar(state.charAt(idx));
    }

    public int adjacentOccupants(int x, int y) {
      int occupants = 0;
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (i == 0 && j == 0) continue;
          int checkX = i + x;
          int checkY = j + y;
          if (checkX < 0 || checkX >= width) continue;
          if (checkY < 0 || checkY >= height) continue;
          if (stateAt(checkX, checkY) == State.OCCUPIED_SEAT) occupants++;
        }
      }
      return occupants;
    }

    public int visibleOccupants(int x, int y) {
      //                    up,dn,rt,lt,diagonals..
      int[] dx = new int[] { 0, 0, 1,-1,-1, 1,-1, 1 };
      int[] dy = new int[] { 1,-1, 0, 0,-1, 1, 1,-1 };
      int maxDistance = Math.max(width, height);
      int occupants = 0;

      for (int k = 0; k < dx.length; k++) {
        for (int d = 1; d <= maxDistance; d++) {
          int checkX = x + (dx[k] * d);
          int checkY = y + (dy[k] * d);
          if (checkX < 0 || checkX >= width) break;
          if (checkY < 0 || checkY >= height) break;
          State state = stateAt(checkX, checkY);
          if (state == State.OCCUPIED_SEAT) {
//            if (x == 0 && y == 1) {
//              LOGGER.info("checkX: {}, checkY: {}, k: {}, d: {}", checkX, checkY, k, d);
//            }
            occupants++;
            break;
          } else if (state == State.EMPTY_SEAT) {
            break;
          }
        }
      }
//      LOGGER.info("x: {}, y: {}, o: {}", x, y, occupants);
      return occupants;
    }

    public int checkOccupancy(int x, int y) {
      if (useAdjacency) return adjacentOccupants(x, y);
      else return visibleOccupants(x, y);
    }

    public int occupiedSeats() {
      return (int)state.chars().filter(i -> i == State.OCCUPIED_SEAT.marker).count();
    }

    public Room nextGeneration() {
      StringBuilder nextRoom = new StringBuilder();
      for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
          State currentState = stateAt(i, j);
          switch (currentState) {
          case EMPTY_SEAT:
            if (checkOccupancy(i, j) == 0) {
              nextRoom.append(State.OCCUPIED_SEAT.marker);
            } else {
              nextRoom.append(State.EMPTY_SEAT.marker);
            }
            break;
          case OCCUPIED_SEAT:
            if (checkOccupancy(i, j) >= occupancyThreshold) {
              nextRoom.append(State.EMPTY_SEAT.marker);
            } else {
              nextRoom.append(State.OCCUPIED_SEAT.marker);
            }
            break;
          default:
            nextRoom.append(State.FLOOR.marker);
            break;
          }
        }
      }
      return new Room(width, height, useAdjacency, nextRoom.toString());
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + height;
      result = prime * result + ((state == null) ? 0 : state.hashCode());
      result = prime * result + width;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Room other = (Room) obj;
      if (height != other.height)
        return false;
      if (state == null) {
        if (other.state != null)
          return false;
      } else if (!state.equals(other.state))
        return false;
      if (width != other.width)
        return false;
      return true;
    }
  }
}
