package org.geekden.advent.solution.y2020.d14;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2020, 14);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    ComputerVersionOne computer = new ComputerVersionOne();
    List<String> lines = input.collect(Collectors.toList());
    for (String command : lines) {
      if (command.startsWith("mask = ")) {
        computer.setMask(command.substring(7));
      } else {
        long memAddress = Long.parseLong(command.split("\\]|\\[")[1]);
        long value = Long.parseLong(command.split(" = ")[1]);
        computer.setMemory(memAddress, value);
      }
    }
    return String.valueOf(computer.sumMemory());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    return null;
  }

  static class ComputerVersionOne {
    // address -> value
    final Map<Long, Long> memory = new HashMap<>();
    BitSet orMask; // mask bits ON
    BitSet andMask; // mask bits OFF

    // Eg 100X100X101011111X100000100X11010011
    void setMask(String mask) {
      char[] characters = mask.toCharArray();
      orMask = new BitSet();
      andMask = new BitSet();

      for (int i = characters.length; i > 0; i--) {
        char character = characters[i - 1];
        orMask.set(characters.length - i, character == '1');
        andMask.set(characters.length - i, character != '0');
      }

      LOGGER.debug("Mask: {}", mask);
      LOGGER.debug(String.format("  OR: %36s", Long.toBinaryString(orMask.toLongArray()[0])));
      LOGGER.debug(String.format(" AND: %36s", Long.toBinaryString(andMask.toLongArray()[0])));
    }

    void setMemory(long address, long value) {
      BitSet bitValue = BitSet.valueOf(new long[] { value });
      bitValue.and(andMask);
      bitValue.or(orMask);

      memory.put(address, bitValue.toLongArray()[0]);
      LOGGER.debug("Mem Input:  {}", value);
      LOGGER.debug("Mem Output: {}", memory.get(address));
    }

    long sumMemory() {
      return memory.values().stream().mapToLong(l -> l).sum();
    }
  }

}
