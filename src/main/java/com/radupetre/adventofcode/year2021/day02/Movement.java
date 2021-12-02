package com.radupetre.adventofcode.year2021.day02;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Movement {
  private MovementType movementType;
  private Integer movementValue;

  public Movement(String movement) {
    final String[] movementParts = movement.split(" ");
    this.movementType = MovementType.valueOf(movementParts[0].toUpperCase());
    this.movementValue = Integer.parseInt(movementParts[1]);
  }
}

enum MovementType {
  FORWARD, UP, DOWN
}
