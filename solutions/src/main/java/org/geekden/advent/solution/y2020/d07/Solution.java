package org.geekden.advent.solution.y2020.d07;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;

public class Solution extends Solver {
  private Graph<String, DefaultWeightedEdge> graph;

  public Solution() {
    super(2020, 7, "Handy Haversacks");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    graph = parseRulesAsGraph(input);

    long count = traverseContainersOf("shiny gold")
      .distinct()
      .count() - 1;
    return String.valueOf(count);
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    graph = parseRulesAsGraph(input);

    long count = traverseContents(new AbstractMap.SimpleEntry<>("shiny gold", 1))
      .mapToLong(e -> e.getValue().longValue())
      .sum() - 1;
    return String.valueOf(count);
  }

  private Stream<String> traverseContainersOf(String bag) {
    Set<DefaultWeightedEdge> incomingEdges = graph.incomingEdgesOf(bag);
    if (incomingEdges.isEmpty()) {
      return Stream.of(bag);
    } else {
      return Streams.concat(
          Stream.of(bag),
          incomingEdges.stream()
            .map(e -> graph.getEdgeSource(e))
            .flatMap(this::traverseContainersOf));
    }
  }

  private Stream<Map.Entry<String, Integer>> traverseContents(Map.Entry<String, Integer> bagEntry) {
    Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(bagEntry.getKey());
    if (outgoingEdges.isEmpty()) {
      return Stream.of(bagEntry);
    } else {
      return Streams.concat(
          Stream.of(bagEntry),
          outgoingEdges.stream()
            .map(e -> new AbstractMap.SimpleEntry<>(
                graph.getEdgeTarget(e), (int)graph.getEdgeWeight(e) * bagEntry.getValue()))
            .flatMap(this::traverseContents));
    }
  }

  private Graph<String, DefaultWeightedEdge> parseRulesAsGraph(Stream<String> input) {
    Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    input.forEach(l -> {
        Sentence sentence = new Sentence(l);
        String bagName = sentence.takeWords(2);
        sentence.takeWords(2); // "bags contain"

        g.addVertex(bagName);
        if (!sentence.discardWordsIfMatch("no", "other", "bags")) {
          while (!sentence.isEmpty()) {
            int bagContainsCount = Integer.parseInt(sentence.takeWords(1));
            String bagContainsName = sentence.takeWords(2);
            g.addVertex(bagContainsName);
            sentence.takeWords(1); // "bag" or "bags"
            Graphs.addEdge(g, bagName, bagContainsName, bagContainsCount);
          }
        }
    });
    return g;
  }

  static class Sentence {
    final List<String> words;

    Sentence(String sentence) {
      this.words = Arrays.stream(sentence.split(", | |\\.")).collect(Collectors.toCollection(LinkedList::new));
    }

    String takeWords(int count) {
      Preconditions.checkArgument(count <= words.size(), String.format("Count (%d) > size (%d).", count, words.size()));

      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < count; i++) {
        builder.append(words.remove(0));
        if (i + 1 < count) builder.append(" ");
      }
      return builder.toString();
    }

    boolean discardWordsIfMatch(String... words) {
      if (words.length > this.words.size()) return false;
      for (int i = 0; i < words.length; i++) {
        if (!words[i].equals(this.words.get(i))) return false;
      }
      for (int i = 0; i < words.length; i++) {
        this.words.remove(0);
      }
      return true;
    }

    boolean isEmpty() {
      return this.words.isEmpty();
    }
  }
}
