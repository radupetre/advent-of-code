package com.radupetre.adventofcode.year2020.day12;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.stream.Collectors;
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
    final List<Instruction> instructions = parseInstructions(allInstructions);
    return calculateDistanceAfterShipMovement(instructions);
  }

  @Override
  public Object solvePart2(String allInstructions) {
    final List<Instruction> instructions = parseInstructions(allInstructions);
    return calculateDistanceAfterShipAndWaypointMovement(instructions);
  }

  private List<Instruction> parseInstructions(String allInstructions) {
    return getLines(allInstructions).stream()
        .map(Instruction::new)
        .collect(Collectors.toList());
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

