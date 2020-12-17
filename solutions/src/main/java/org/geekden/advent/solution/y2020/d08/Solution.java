package org.geekden.advent.solution.y2020.d08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geekden.advent.framework.Solver;

import com.google.common.base.MoreObjects;

public class Solution extends Solver {

  public Solution() {
    super(2020, 8, "Handheld Halting");
  }

  @Override
  public String solvePartOne(Stream<String> input) {
    Computer computer = new Computer(parseProgram(input));
    try {
      computer.run();
    } catch (IllegalStateException e) {
      // crash is expected!
    }
    return String.valueOf(computer.accumulator);
  }

  @Override
  public String solvePartTwo(Stream<String> input) {
    List<Instruction> program = parseProgram(input);
    Computer computer = new Computer(new ArrayList<>(program));
    int potentialCorruptedOffset = 0;
    while (potentialCorruptedOffset <= computer.program.size()) {
      for (int i = potentialCorruptedOffset; i < computer.program.size(); i++) {
        Instruction instr = computer.program.get(i);
        if (instr.op == Operation.ACC) continue;
        potentialCorruptedOffset = i + 1;
        computer.program.set(
            i,
            new Instruction(
                instr.offset,
                instr.op == Operation.JMP ? Operation.NOP : Operation.JMP,
                instr.parameter));
        break;
      }

      try {
        computer.run();
        break;
      } catch (IllegalStateException e) {
        // still crashing, reset state & try again...
        computer = new Computer(new ArrayList<>(program));
      }
    }

    return String.valueOf(computer.accumulator);
  }

  List<Instruction> parseProgram(Stream<String> input) {
    AtomicInteger offsetCounter = new AtomicInteger();
    return input.map(l -> {
        String[] opAndParameter = l.split(" ");
        return new Instruction(offsetCounter.getAndIncrement(), Operation.valueOf(opAndParameter[0].toUpperCase()), Long.valueOf(opAndParameter[1]));
      })
      .collect(Collectors.toList());
  }

  static class Computer {
    long accumulator = 0l;
    int instructionPointer = -1;
    Instruction lastInstr;
    List<Instruction> program;

    Computer(List<Instruction> program) {
      this.program = program;
      for (Instruction instruction : program) {
        instruction.executed = false;
      }
    }

    void run() {
      while (advance()) {
        debug();
      }
    }

    boolean advance() {
      instructionPointer++;
      if (instructionPointer == program.size()) {
        return false; // normal exit
      }

      Instruction instr = program.get(instructionPointer);

      if (instr.executed) {
        throw new IllegalStateException("Infinite loop detected!");
      }

      switch (instr.op) {
      case ACC:
        accumulator += instr.parameter;
        break;
      case JMP:
        instructionPointer += instr.parameter - 1;
        break;
      default:
        break;
      }
      instr.executed = true;
      lastInstr = instr;
      return true;
    }

    void debug() {
      LOGGER.debug("{}: acc: {}, istr: {}", lastInstr, accumulator, instructionPointer);
    }
  }

  enum Operation {
    ACC, JMP, NOP
  }

  static class Instruction {
    final int offset;
    final Operation op;
    final long parameter;
    boolean executed = false;

    public Instruction(int offset, Operation op, long parameter) {
      this.offset = offset;
      this.op = op;
      this.parameter = parameter;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(Instruction.class)
          .add("op", op).add("parameter", parameter).add("executed", executed)
          .toString();
    }
  }
}
