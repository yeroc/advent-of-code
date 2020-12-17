package org.geekden.advent.solution.y2020.d04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

import one.util.streamex.StreamEx;

public class Solution extends Solver {

  public Solution() {
    super(2020, 4, "Passport Processing");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    long count = parse(input).filter(Solution::hasRequiredFields).count();
    return String.valueOf(count);
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    List<Predicate<Passport>> rules = partTwoPassportRules();
    long count = parse(input)
        .filter(rules.stream().reduce(x -> true, Predicate::and))
        .count();
    return String.valueOf(count);
  }

  private static boolean hasRequiredFields(Passport p) {
    List<String> requiredFields = Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    for (String field : requiredFields) {
      if (!p.fields.containsKey(field)) return false;
    }
    return true;
  }

  private static List<Predicate<Passport>> partTwoPassportRules() {
    List<Predicate<Passport>> rules = new ArrayList<>();
    rules.add(Solution::hasRequiredFields);
    rules.add(p -> {
      var byr = Integer.valueOf(p.fields.get("byr"));
      return byr >= 1920 && byr <= 2002;
    });
    rules.add(p -> {
      var iyr = Integer.valueOf(p.fields.get("iyr"));
      return iyr >= 2010 && iyr <= 2020;
    });
    rules.add(p -> {
      var eyr = Integer.valueOf(p.fields.get("eyr"));
      return eyr >= 2020 && eyr <= 2030;
    });
    rules.add(p -> {
      var hgt = p.fields.get("hgt");
      var matcher = Pattern.compile("(\\d+)(cm|in)").matcher(hgt);
      if (matcher.find()) {
        var value = Integer.valueOf(matcher.group(1));
        if ("in".equals(matcher.group(2))) {
          return value >= 59 && value <= 76;
        } else { // cm
          return value >= 150 && value <= 193;
        }
      } else {
        return false;
      }
    });
    rules.add(p -> p.fields.get("hcl").matches("#([0-9a-f]){6}"));
    rules.add(p -> p.fields.get("ecl").matches("amb|blu|brn|gry|grn|hzl|oth"));
    rules.add(p -> p.fields.get("pid").matches("\\d{9}"));

    return rules;
  }

  private Stream<Passport> parse(Stream<String> input) {
    return StreamEx.of(input)
      .collapse((a, b) -> !a.isEmpty(), Collectors.joining(" "))
      .map(Solution::parsePassport);
  }

  private static Passport parsePassport(String line) {
    String[] fieldValues = line.split(" ");
    Passport passport = new Passport();
    for (String fieldAndValue : fieldValues) {
      String[] fields = fieldAndValue.split(":");
      passport.fields.put(fields[0], fields[1]);
    }
    return passport;
  }

  static class Passport {
    final Map<String, String> fields = new HashMap<>();
  }
}
