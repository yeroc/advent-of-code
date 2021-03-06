package org.geekden.advent.solution.y2020.d05;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

import com.google.common.base.MoreObjects;

public class Solution extends Solver {

  public Solution() {
    super(2020, 5, "Binary Boarding");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    BoardingPass highest = input.map(Solution::parseBoardingPass)
        .sorted(Comparator.comparing(BoardingPass::seatId).reversed())
        .findFirst()
        .orElse(null);
    return String.valueOf(highest.seatId());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    AtomicReference<BoardingPass> previousReference = new AtomicReference<>();
    BoardingPass output = input.map(Solution::parseBoardingPass)
        .sorted(Comparator.comparing(BoardingPass::seatId))
        .filter(s -> s.row > 1 && s.row < 127)
        .filter(s -> {
          BoardingPass previousBoardingPass = previousReference.getAndSet(s);
          return !(previousBoardingPass == null || previousBoardingPass.seatId() + 1 == s.seatId());
        })
        .findFirst()
        .orElse(null);
    // the above returns the seat after the empty one...
    return String.valueOf(output.seatId() - 1);
  }

  private static BoardingPass parseBoardingPass(String line) {
    return new BoardingPass(decodeAsBinary(line.substring(0, 7)), decodeAsBinary(line.substring(7, 10)));
  }

  /** F or L = 0, B or R = 1 */
  private static long decodeAsBinary(String binary) {
    binary = binary
       .replace('F', '0')
       .replace('B', '1')
       .replace('L', '0')
       .replace('R', '1');
    return Long.parseLong(binary, 2);
  }

  static class BoardingPass {
    final long row;
    final long column;

    public BoardingPass(long row, long column) {
      this.row = row;
      this.column = column;
    }

    public long seatId() {
      return (row * 8) + column;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(BoardingPass.class).add("row", row).add("column", column).add("seatId", seatId()).toString();
    }
  }
}
