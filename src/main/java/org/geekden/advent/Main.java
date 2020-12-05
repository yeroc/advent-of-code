package org.geekden.advent;

import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name="advent", mixinStandardHelpOptions = true, versionProvider = Version.class, description = "Runs solutions to Advent of Code (https://adventofcode.com/).")
class Main implements Callable<Integer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private static final ZoneId ADVENT_TIME_ZONE = ZoneId.of("UTC-5");

  private static Set<String> targets = new HashSet<>();

  // TODO figure out Graalvm native image? https://github.com/SoftInstigate/classgraph-on-graalvm
  static {
    try (ScanResult scanResult = new ClassGraph()
        .disableModuleScanning() // added for GraalVM
        // see https://github.com/oracle/graal/issues/470#issuecomment-401022008
        .addClassLoader(ClassLoader.getSystemClassLoader())
        .enableAnnotationInfo()
        .enableMethodInfo()
        .initializeLoadedClasses()
        .scan()) {

      ClassInfoList dayClasses = scanResult.getSubclasses(Solver.class.getName()).getStandardClasses();

      LOGGER.debug("Day classes " + dayClasses.getNames());

      for (ClassInfo clazz : dayClasses) {
        targets.add(clazz.getName());
      }
    }
  }

  public static void main(String[] args) throws Exception {
    int exitCode = new CommandLine(new Main()).execute(args);
    System.exit(exitCode);
  }

  @Option(names = { "--year", "-y" }, description = "Run solvers for the specified year only.")
  private Integer yearFilter = -1;

  @Option(names = { "--day", "-d" }, description = "Run solvers for the specified day only.")
  private Integer dayFilter = -1;

  @Option(names = { "--now", "-n" }, description = "Run solvers for today only (takes precedence over any year or day filters).")
  private boolean nowFilter = false;

  @Override
  public Integer call() throws Exception {
    if (targets.isEmpty()) {
      System.err.println("No days implemented!");
      return 1;
    }

    SortedSet<Solver> days = new TreeSet<>();
    for (String className : targets) {
      @SuppressWarnings("unchecked")
      Class<Solver> clazz = (Class<Solver>)Class.forName(className);
      days.add(clazz.getDeclaredConstructor().newInstance());
    }

    if (nowFilter) {
      ZonedDateTime now = ZonedDateTime.now(ADVENT_TIME_ZONE);
      yearFilter = now.getYear();
      dayFilter = now.getDayOfMonth();
    }

    new Driver(
        days.stream()
            .filter(d -> yearFilter < 0 || d.year() == yearFilter)
            .filter(d -> dayFilter < 0 || d.day() == dayFilter)
            .collect(Collectors.toCollection(TreeSet::new)), // use TreeSet to keep them sorted
        Paths.get("src/main/resources")).execute();

    return 0;
  }
}
