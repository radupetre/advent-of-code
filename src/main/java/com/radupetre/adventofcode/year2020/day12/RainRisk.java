package com.radupetre.adventofcode.year2020.day12;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Math.abs;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/12
 */
@Service
@Log4j2
public class RainRisk extends AbstractAdventSolution {


  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 12);
  }

  @Override
  public Object solvePart1(String allInstructions) {
    final List<Instruction> instructions = getLines(allInstructions).stream()
        .map(Instruction::new)
        .collect(Collectors.toList());

    return calculateDistanceAfterShipMovement(instructions);
  }

  @Override
  public Object solvePart2(String allInstructions) {
    final List<Instruction> instructions = getLines(allInstructions).stream()
        .map(Instruction::new)
        .collect(Collectors.toList());

    return calculateDistanceAfterShipAndWaypointMovement(instructions);
  }

  private long calculateDistanceAfterShipAndWaypointMovement(List<Instruction> instructions) {
    // performs ship & waypoint movements
    ShipAndWaypoint shipAndWaypoint = new ShipAndWaypoint();
    instructions.stream()
        .forEach(shipAndWaypoint::move);
    return shipAndWaypoint.calculateDistance();
  }

  private long calculateDistanceAfterShipMovement(List<Instruction> instructions) {
    // performs ship only movements
    Ship ship = new Ship();
    instructions.stream()
        .forEach(ship::move);
    return ship.calculateDistance();
  }
}

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

@AllArgsConstructor
class Instruction {

  final char action;
  final int value;

  Instruction(String instruction) {
    this.action = instruction.charAt(0);
    this.value = Integer.parseInt(instruction.substring(1));
  }
}
