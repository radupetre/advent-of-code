package com.radupetre.adventofcode.year2020.day12;

import static java.lang.Math.abs;

class Ship {

  private static final char[] ROTATE_DIRECTIONS = {'E', 'S', 'W', 'N'};
  static int DEGREES_IN_TURN = 90;

  int shipLat = 0;
  int shipLon = 0;
  private int shipRotation = 0;

  void move(Instruction instruction) {
    switch (instruction.action) {
      case 'N' -> shipLat += instruction.value;
      case 'S' -> shipLat -= instruction.value;
      case 'E' -> shipLon += instruction.value;
      case 'W' -> shipLon -= instruction.value;
      case 'R' -> shipRotation += instruction.value / DEGREES_IN_TURN;
      case 'L' -> shipRotation -= instruction.value / DEGREES_IN_TURN;
      case 'F' -> {
        shipRotation %= ROTATE_DIRECTIONS.length;
        if (shipRotation < 0) {
          shipRotation += ROTATE_DIRECTIONS.length;
        }
        char shipDirection = ROTATE_DIRECTIONS[shipRotation];
        this.move(new Instruction(shipDirection, instruction.value));
      }
    }
  }

  int calculateDistance() {
    return abs(shipLat) + abs(shipLon);
  }
}
