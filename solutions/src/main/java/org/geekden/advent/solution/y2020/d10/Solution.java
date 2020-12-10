package org.geekden.advent.solution.y2020.d10;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2020, 10);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    AtomicLong oneJoltDeltas = new AtomicLong();
    AtomicLong threeJoltDeltas = new AtomicLong();
    AtomicLong lastValue = new AtomicLong(0L);
    parseInput(input)
      .sorted()
      .peek(l -> LOGGER.debug("{}", l))
      .forEach(v -> {
        long previous = lastValue.getAndSet(v);
        if (v - previous == 1) {
          oneJoltDeltas.incrementAndGet();
        } else if (v - previous == 3) {
          threeJoltDeltas.incrementAndGet();
        }
      });

    threeJoltDeltas.incrementAndGet(); // the device adaptor...
    return String.valueOf(oneJoltDeltas.get() * threeJoltDeltas.get());
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    List<Long> voltages = parseInput(input).sorted().boxed().collect(Collectors.toList());
    Long startVoltage = 0l;
    Long endVoltage = voltages.get(voltages.size() - 1) + 3;
    voltages.add(0, startVoltage);
    voltages.add(endVoltage);

    long[] adapterVoltages = voltages.stream().mapToLong(l -> l).toArray();
    long[] adapterPossibilities = new long[adapterVoltages.length];

    adapterPossibilities[0] = 1;
    for (int i = 1; i < adapterPossibilities.length; i++) {
      // returns a value between 1 & 3 telling us how many previous adapters need to be considered
      int possibilities = findPossibilities(adapterVoltages, i);

      for (int j = i - 1; j >= Math.max(0, i - possibilities); j--) {
        adapterPossibilities[i] += adapterPossibilities[j];
      }
    }
    LOGGER.debug("{}", Arrays.toString(adapterVoltages));
    LOGGER.debug("{}", Arrays.toString(adapterPossibilities));

    return String.valueOf(adapterPossibilities[adapterPossibilities.length - 1]);
  }

  private int findPossibilities(long[] adapterVoltages, int adapterIdx) {
    int count = 0;
    for (int i = adapterIdx - 1; i >= Math.max(0, adapterIdx - 4); i--) {
      if (adapterVoltages[adapterIdx] - adapterVoltages[i] <= 3) {
        count++;
      } else {
        break;
      }
    }
    return count;
  }

  LongStream parseInput(Stream<String> input) {
    return input.mapToLong(Long::parseLong);
  }
}
