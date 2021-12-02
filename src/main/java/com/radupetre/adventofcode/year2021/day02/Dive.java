package com.radupetre.adventofcode.year2021.day02;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/2
 */
@Service
@Log4j2
public class Dive extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 2);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Movement> movements = getMovements(input);
    int depthPosition = 0, horizontalPosition = 0;

    for (Movement movement : movements) {
      switch (movement.getMovementType()) {
        case UP -> depthPosition -= movement.getMovementValue();
        case DOWN -> depthPosition += movement.getMovementValue();
        case FORWARD -> horizontalPosition += movement.getMovementValue();
      }
    }

    return depthPosition * horizontalPosition;
  }

  @Override
  public Object solvePart2(String input) {
    final List<Movement> movements = getMovements(input);
    int depthPosition = 0, horizontalPosition = 0, currentAim = 0;

    for (Movement movement : movements) {
      switch (movement.getMovementType()) {
        case UP -> currentAim -= movement.getMovementValue();
        case DOWN -> currentAim += movement.getMovementValue();
        case FORWARD -> {
          horizontalPosition += movement.getMovementValue();
          depthPosition += currentAim * movement.getMovementValue();
        }
      }
    }

    return depthPosition * horizontalPosition;
  }

  List<Movement> getMovements(String input) {
    return getLines(input)
        .stream()
        .map(Movement::new)
        .collect(Collectors.toList());
  }

}
