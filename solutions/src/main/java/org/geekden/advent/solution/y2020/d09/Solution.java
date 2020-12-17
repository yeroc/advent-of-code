package org.geekden.advent.solution.y2020.d09;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.OptionalLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Sets;

/**
 * Works but still needs cleanup!
 */
public class Solution extends Solver {

  private long sumToFind;

  public Solution() {
    super(2020, 9, "Encoding Error");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    EvictingQueue<Long> elementHistory = EvictingQueue.create(25);

    sumToFind = parseInput(input)
      .flatMap(value -> {
        if (elementHistory.remainingCapacity() > 0) {
          elementHistory.add(value);
          return LongStream.empty();
        } else {
          OptionalLong found = Sets.combinations(Sets.newHashSet(elementHistory), 2)
            .stream()
            .flatMapToLong(s -> {
              Iterator<Long> longs = s.iterator();
              return LongStream.of(longs.next() + longs.next());
            })
            .filter(sum -> sum == value)
            .findFirst();

          if (found.isPresent()) {
            elementHistory.add(value);
            return LongStream.empty();
          } else {
            return LongStream.of(value);
          }
        }
      }).findFirst().getAsLong();
    return String.valueOf(sumToFind); // save for solving part II
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    LinkedList<Long> contiguousLongs = new LinkedList<>();

    long lastValue = parseInput(input).flatMap(value -> {
      contiguousLongs.add(value);
      long sum = 0L;
      while ((sum = contiguousLongs.stream().mapToLong(l -> l).sum()) > sumToFind) {
        contiguousLongs.removeFirst();
      }

      if (sum == sumToFind) {
        return LongStream.of(value);
      } else {
        return LongStream.empty();
      }
    }).findFirst().getAsLong();

    LOGGER.debug("{}", contiguousLongs);
    long minimum = contiguousLongs.stream().mapToLong(l -> l).min().getAsLong();
    long maximum = contiguousLongs.stream().mapToLong(l -> l).max().getAsLong();
    return String.valueOf(minimum + maximum);
  }

  LongStream parseInput(Stream<String> input) {
    return input.mapToLong(Long::parseLong);
  }

}
