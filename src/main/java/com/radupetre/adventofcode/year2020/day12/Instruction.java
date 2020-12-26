package com.radupetre.adventofcode.year2020.day12;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Instruction {

  final char action;
  final int value;

  Instruction(String instruction) {
    this.action = instruction.charAt(0);
    this.value = Integer.parseInt(instruction.substring(1));
  }
}
