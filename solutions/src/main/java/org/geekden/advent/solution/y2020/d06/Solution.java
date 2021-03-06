package org.geekden.advent.solution.y2020.d06;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

import one.util.streamex.StreamEx;

public class Solution extends Solver {

  private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

  public Solution() {
    super(2020, 6, "Custom Customs");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    long sum = parse(input)
        .map(g -> g.answers)
        .mapToLong(s -> s.chars().distinct().count())
        .sum();
    return String.valueOf(sum);
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    long sum = parse(input)
      .mapToLong(g ->
        Arrays.stream(g.answers.split(""))
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet().stream()
                    .filter(e -> e.getValue() >= g.members)
                    .count())
      .sum();
    return String.valueOf(sum);
  }

  Stream<Group> parse(Stream<String> input) {
    return StreamEx.of(input)
        .collapse((a, b) -> !a.isEmpty(), Collectors.joining(" "))
        .map(Solution::parseGroup);
  }

  private static Group parseGroup(String line) {
    Matcher m = SPACE_PATTERN.matcher(line);
    long members = m.results().count();
    String answers = line.replace(" ", "");
    return new Group(members, answers);
  }

  static class Group {
    final long members;
    final String answers;

    Group(long members, String answers) {
      this.members = members;
      this.answers = answers;
    }
  }
}
