package com.radupetre.adventofcode.year2020.day12;

import java.util.stream.IntStream;

class ShipAndWaypoint extends Ship {

  private int waypointLat = 1;
  private int waypointLon = 10;

  void move(Instruction instruction) {
    switch (instruction.action) {
      case 'N' -> waypointLat += instruction.value;
      case 'S' -> waypointLat -= instruction.value;
      case 'E' -> waypointLon += instruction.value;
      case 'W' -> waypointLon -= instruction.value;
      case 'R' -> IntStream
          .range(0, getRotationsCount(instruction))
          .forEach(this::rotateWaypointRight);
      case 'L' -> IntStream
          .range(0, getRotationsCount(instruction))
          .forEach(this::rotateWaypointLeft);
      case 'F' -> {
        shipLat += instruction.value * waypointLat;
        shipLon += instruction.value * waypointLon;
      }
    }
  }

  int getRotationsCount(Instruction instruction) {
    return instruction.value / DEGREES_IN_TURN;
  }

  private void rotateWaypointRight(int __) {
    int reversedWaypointLon = -waypointLon;
    waypointLon = waypointLat;
    waypointLat = reversedWaypointLon;
  }

  private void rotateWaypointLeft(int __) {
    int reversedWaypointLat = -waypointLat;
    waypointLat = waypointLon;
    waypointLon = reversedWaypointLat;
  }
}
