package org.geekden.advent.solution.y2020.d13;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2020, 13, "Shuttle Search");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    List<String> lines = input.collect(Collectors.toList());
    long targetTime = Long.parseLong(lines.get(0));
    var result = Stream.of(lines.get(1).split(","))
        .filter(s -> !s.equals("x"))
        .mapToInt(Integer::parseInt)
        .mapToObj(b -> new AbstractMap.SimpleImmutableEntry<>(b, b - (targetTime % b)))
        .sorted(Map.Entry.comparingByValue())
        .findFirst().get();

    return String.valueOf(result.getKey() * result.getValue());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    List<String> lines = input.collect(Collectors.toList());
    var results = lines.get(1).split(",");

    SortedMap<Long, Long> targets = new TreeMap<>(Collections.reverseOrder());

    for (int i = 0; i < results.length; i++) {
      String value = results[i];
      if ("x".equals(value)) continue;
      targets.put(Long.parseLong(value), (long)i);
    }

    // credit to lizthegrey (https://www.twitch.tv/videos/835702252)...
    long minValue = 0l;
    long runningProduct = 1l;

    for (Entry<Long, Long> entry : targets.entrySet()) {
      long k = entry.getKey();
      long v = entry.getValue();
      while ( (minValue + v) % k != 0) {
        minValue += runningProduct;
      }
      runningProduct *= k;
//      System.out.printf("Sum so far: %d, product so far: %d\n", minValue, runningProduct);
    }
    return String.valueOf(minValue);
  }
}
