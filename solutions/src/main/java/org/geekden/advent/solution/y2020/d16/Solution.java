package org.geekden.advent.solution.y2020.d16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.geekden.advent.Solver;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class Solution extends Solver {

  public Solution() {
    super(2020, 16);
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    Facts facts = parseInput(input);

    int errors = facts.nearbyTickets.stream()
        .flatMapToInt(facts::invalidValues)
        .sum();
    return String.valueOf(errors);
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    Facts facts = parseInput(input);

    List<Ticket> validTickets = facts.nearbyTickets.stream()
        .filter(facts::isValid)
        .collect(Collectors.toList());

    validTickets.add(facts.yourTicket);

    Set<Integer> unsolvedFields = IntStream.range(0, facts.yourTicket.fieldCount())
        .mapToObj(Integer::valueOf)
        .collect(Collectors.toSet());
    Map<String, Integer> fieldNameToIdx = new HashMap<>();
    while (!unsolvedFields.isEmpty()) {
      for (Iterator<Integer> unsolvedFieldsIterator = unsolvedFields.iterator(); unsolvedFieldsIterator.hasNext();) {
        Integer fieldIdx = unsolvedFieldsIterator.next();
        Set<String> fields = findCandidateFields(fieldIdx, validTickets, facts.valueToField);
        if (fields.size() == 1) {
          String field = fields.stream().findFirst().get();
          LOGGER.debug("Solved field for idx {} is {}", fieldIdx, field);
          unsolvedFieldsIterator.remove();
          fieldNameToIdx.put(field, fieldIdx);
          while (facts.valueToField.values().remove(field)) {}
        }
      }
    }

    long result = fieldNameToIdx.entrySet().stream()
        .filter(e -> e.getKey().startsWith("departure "))
        .map(e -> facts.yourTicket.fieldValue(e.getValue()))
        .mapToLong(l -> l)
        .reduce(1, (a, b) -> a * b);

    return String.valueOf(result);
  }

  private static Set<String> findCandidateFields(int index, List<Ticket> tickets, Multimap<Integer, String> rules) {
    Set<String> fields = new HashSet<>(rules.values());
    for (Ticket ticket : tickets) {
      Integer fieldValue = ticket.fieldValue(index);
      Set<String> possibleFields = new HashSet<>(rules.get(fieldValue));

      fields = new HashSet<>(Sets.intersection(possibleFields, fields));
      if (fields.size() == 1) {
        break;
      }
    }
    return fields;
  }

  private static Facts parseInput(Stream<String> input) {
    Facts facts = new Facts();
    List<String> inputs = input.collect(Collectors.toList());

    // parse field rules...
    int i = 0;
    for (i = 0; i < inputs.size(); i++) {
      String line = inputs.get(i);
      if (Strings.isNullOrEmpty(line)) {
        break;
      }
      facts.parseRule(line);
    }

    i += 2;
    // your ticket
    facts.yourTicket = parseTicket(inputs.get(i));

    i += 3;
    // nearby tickets
    for (; i < inputs.size(); i++) {
      String line = inputs.get(i);
      facts.nearbyTickets.add(parseTicket(line));
    }
    return facts;
  }

  private static Ticket parseTicket(String line) {
    String[] values = line.split(",");
    Ticket ticket = new Ticket();
    for (String value : values) {
      ticket.addFieldValue(Integer.valueOf(value));
    }
    return ticket;
  }

  static class Facts {
    // valid values -> fields
    Multimap<Integer, String> valueToField = ArrayListMultimap.create();
    Ticket yourTicket;
    List<Ticket> nearbyTickets = new ArrayList<>();

    void parseRule(String line) {

      String[] s = line.split(": ");
      String field = s[0];
      String[] ranges = s[1].split(" or ");
      String[] rangeOne = ranges[0].split("-");
      String[] rangeTwo = ranges[1].split("-");
      IntStream
          .concat(
              IntStream.rangeClosed(Integer.parseInt(rangeOne[0]), Integer.parseInt(rangeOne[1])),
              IntStream.rangeClosed(Integer.parseInt(rangeTwo[0]), Integer.parseInt(rangeTwo[1])))
          .forEach(i -> valueToField.put(i, field));
    }

    IntStream invalidValues(Ticket ticket) {
      return ticket.stream()
          .filter(i -> !valueToField.containsKey(i))
          .mapToInt(i -> i);
    }

    boolean isValid(Ticket ticket) {
      return invalidValues(ticket).findFirst().isEmpty();
    }
  }

  static class Ticket {
    private List<Integer> fieldValues = new ArrayList<>();

    Stream<Integer> stream() {
      return fieldValues.stream();
    }

    void addFieldValue(int value) {
      fieldValues.add(value);
    }

    int fieldValue(int index) {
      return fieldValues.get(index);
    }

    int fieldCount() {
      return fieldValues.size();
    }
  }
}
