package org.geekden.advent.solution.y2020.d02;

import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2020, 2);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    return String.valueOf(
        parseInput(input).filter(Solution::pwdIsValidPartOne).count());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    return String.valueOf(
        parseInput(input).filter(Solution::pwdIsValidPartTwo).count());
  }

  private static boolean pwdIsValidPartOne(PasswordEntry p) {
    long count = p.pwd.chars()
        .filter(c -> c == p.character)
        .count();
    return count >= p.paramOne && count <= p.paramTwo;
  }

  private static boolean pwdIsValidPartTwo(PasswordEntry p) {
    int count = p.pwd.charAt(p.paramOne - 1) == p.character ? 1 : 0;
    count += p.pwd.charAt(p.paramTwo - 1) == p.character ? 1 : 0;
    return count == 1;
  }

  private Stream<PasswordEntry> parseInput(Stream<String> input) {
    return
        input.map(l -> {
          String[] line = l.split("[: -]+");
          return new PasswordEntry(line[2].charAt(0), Integer.valueOf(line[0]), Integer.valueOf(line[1]), line[3]);
        });
  }

  static class PasswordEntry {
    final char character;
    final int paramOne; /* min count / first character position */
    final int paramTwo; /* max count / last character position */
    final String pwd;

    public PasswordEntry(char character, int paramOne, int paramTwo, String pwd) {
      this.character = character;
      this.paramOne = paramOne;
      this.paramTwo = paramTwo;
      this.pwd = pwd;
    }
  }
}
