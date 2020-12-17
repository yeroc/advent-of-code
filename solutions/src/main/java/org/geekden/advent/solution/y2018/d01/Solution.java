package org.geekden.advent.solution.y2018.d01;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

public class Solution extends Solver {

  public Solution() {
    super(2018, 1);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    int frequencyAdjustment = input.mapToInt(Integer::parseInt).sum();
    return String.valueOf(frequencyAdjustment);
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    final SortedSet<Integer> frequencies = new TreeSet<>();
    final AtomicInteger frequency = new AtomicInteger(0);
    List<String> frequencyDeltas = input.collect(Collectors.toList());
    int firstRepeatedFrequency = Stream.generate(new InfiniteSupplier<>(frequencyDeltas))
        .mapToInt(Integer::parseInt)
        .flatMap(e -> {
          int currentFrequency = frequency.addAndGet(e);
          if (frequencies.contains(currentFrequency)) {
            return IntStream.of(currentFrequency);
          } else {
            frequencies.add(currentFrequency);
            return IntStream.empty();
          }
        })
        .findFirst()
        .orElseThrow();
    return String.valueOf(firstRepeatedFrequency);
  }

  static class InfiniteSupplier<E> implements Supplier<E> {
    private final Collection<E> source;
    private Iterator<E> currentIterator;

    public InfiniteSupplier(Collection<E> source) {
      if (source.isEmpty()) {
        throw new IllegalStateException("Collection can't be empty!");
      }
      this.source = source;
      this.currentIterator = source.iterator();
    }

    @Override
    public E get() {
      if (!currentIterator.hasNext()) {
        currentIterator = source.iterator();
      }
      return currentIterator.next();
    }
  }
}
