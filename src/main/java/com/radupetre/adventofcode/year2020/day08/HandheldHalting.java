package com.radupetre.adventofcode.year2020.day08;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2020.day08.Operation.ACC;
import static com.radupetre.adventofcode.year2020.day08.Operation.JMP;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/8
 */
@Service
@Log4j2
public class HandheldHalting extends AbstractAdventSolution {

  private static final EnumSet<Operation> SWAPPABLE_OPERATIONS = EnumSet.of(JMP, ACC);

  private List<Instruction> instructions;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 8);
  }

  @Override
  public void solve(String allInstructions) {
    instructions = getLines(allInstructions).stream()
        .map(Instruction::new)
        .collect(toList());

    log.info(format("Accumulator before loop: %s", getAccumulatorBeforeFirstLoop()));
    log.info(format("Accumulator without loop: %s", getAccumulatorWithoutLoop()));
  }

  private int getAccumulatorBeforeFirstLoop() {
    Set<Integer> visitedInstructions = new HashSet<>();
    AtomicInteger currentInstructionPosition = new AtomicInteger(0);
    AtomicInteger accumulator = new AtomicInteger(0);

    // stop once it reached a loop
    while (!isLoopInstruction(currentInstructionPosition, visitedInstructions)) {

      visitedInstructions.add(currentInstructionPosition.get());
      movePositionAndAccumulate(
          currentInstructionPosition,
          accumulator
      );
    }

    return accumulator.get();
  }

  private int getAccumulatorWithoutLoop() {
    Set<Integer> visitedInstructions = new HashSet<>();
    AtomicInteger currentInstructionPosition = new AtomicInteger(0);
    AtomicInteger accumulator = new AtomicInteger(0);
    boolean stillInLoop = true;

    // stop once it reached the last instruction
    while (!isLastInstruction(currentInstructionPosition)) {
      Operation currentOperation = instructions.get(currentInstructionPosition.get())
          .getOperation();

      if (stillInLoop && canBeSwapped(currentOperation)
          && isLoopFixed(currentInstructionPosition, visitedInstructions, accumulator)) {
        return accumulator.get();
      }

      visitedInstructions.add(currentInstructionPosition.get());
      movePositionAndAccumulate(
          currentInstructionPosition,
          accumulator
      );
    }

    return accumulator.get();
  }

  private boolean isLoopFixed(AtomicInteger startingInstructionPosition,
      Set<Integer> startingVisitedInstructions, AtomicInteger startingAccumulator) {

    Set<Integer> currentVisitedInstructions = new HashSet<>(startingVisitedInstructions);
    AtomicInteger currentInstructionPosition = new AtomicInteger(startingInstructionPosition.get());
    AtomicInteger currentAccumulator = new AtomicInteger(0);

    while (true) {
      if (isLoopInstruction(currentInstructionPosition, currentVisitedInstructions)) {
        // still in a loop
        return false;
      }

      if (isLastInstruction(currentInstructionPosition)) {
        // broke the loop and found the end, add to external accumulator
        startingAccumulator.addAndGet(currentAccumulator.get());
        return true;
      }

      // swap once at the start position
      boolean swap = currentInstructionPosition.get() == startingInstructionPosition.get();

      currentVisitedInstructions.add(currentInstructionPosition.get());
      movePositionAndAccumulate(
          currentInstructionPosition,
          currentAccumulator,
          swap
      );
    }
  }

  private void movePositionAndAccumulate(AtomicInteger currentInstructionPosition,
      AtomicInteger accumulator, boolean swap) {

    Instruction currentInstruction = swap
        ? swapOperation(getInstruction(currentInstructionPosition))
        : getInstruction(currentInstructionPosition);

    switch (currentInstruction.getOperation()) {
      case ACC -> {
        accumulator.addAndGet(currentInstruction.getArgument());
        currentInstructionPosition.incrementAndGet();
      }
      case JMP -> currentInstructionPosition.addAndGet(currentInstruction.getArgument());
      case NOP -> currentInstructionPosition.incrementAndGet();
    }
  }

  private void movePositionAndAccumulate(AtomicInteger currentInstructionPosition,
      AtomicInteger accumulator) {
    movePositionAndAccumulate(currentInstructionPosition, accumulator, false);
  }

  private Instruction getInstruction(AtomicInteger position) {
    return instructions.get(position.get());
  }

  private boolean isLoopInstruction(AtomicInteger instructionPosition,
      Set<Integer> visitedInstructions) {
    return visitedInstructions.contains(instructionPosition.get());
  }

  private boolean isLastInstruction(AtomicInteger instructionPosition) {
    return instructionPosition.get() == instructions.size() - 1;
  }

  private boolean canBeSwapped(Operation operation) {
    return SWAPPABLE_OPERATIONS.contains(operation);
  }

  private Instruction swapOperation(Instruction instruction) {
    return instruction.withOperation(swapOperation(instruction.getOperation()));
  }

  private Operation swapOperation(Operation operation) {
    if (Operation.NOP.equals(operation)) {
      return JMP;
    } else if (JMP.equals(operation)) {
      return Operation.NOP;
    } else {
      return operation;
    }
  }
}

enum Operation {
  ACC, JMP, NOP;
}

@Getter
@With
@RequiredArgsConstructor
class Instruction {

  private final Operation operation;
  private final int argument;

  Instruction(String instruction) {
    final String[] instructionFragments = instruction.split(" ");
    this.operation = Operation.valueOf(instructionFragments[0].toUpperCase());
    this.argument = Integer.parseInt(instructionFragments[1]);
  }
}