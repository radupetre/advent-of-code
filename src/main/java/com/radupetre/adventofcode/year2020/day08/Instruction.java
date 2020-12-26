package com.radupetre.adventofcode.year2020.day08;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

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
