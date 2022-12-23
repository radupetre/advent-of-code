package com.radupetre.adventofcode.year2022.day23;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Direction.EAST;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Direction.NORTH;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Direction.SOUTH;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Direction.WEST;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Direction.allNeighbours;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Movement.movement;
import static com.radupetre.adventofcode.year2022.day23.UnstableDiffusion.Position.position;
import static java.lang.Math.abs;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/23
 */
@Service
@Log4j2
public class UnstableDiffusion extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 23);
  }

  private static final Character ELF = '#';

  private Set<Position> positions;
  private Deque<Direction> directions;

  @Override
  public Object solvePart1(String input) {
    parsePositions(input);
    resetDirections();

    for (int times = 0; times < 10; times++) {
      executeMovement();
      updateDirections();
    }

    return countFreePositions();
  }

  @Override
  public Object solvePart2(String input) {
    parsePositions(input);
    resetDirections();

    int movementCount = 1;
    while (executeMovement()) {
      movementCount++;
      updateDirections();
    }

    return movementCount;
  }

  public boolean executeMovement() {
    Collection<Movement> movements = new LinkedList<>();

    for (var position : positions) {
      if (!areAllAvailable(allNeighbours(position))) {
        for (var direction : directions) {
          if (areAllAvailable(direction.directionNeighbours(position))) {
            movements.add(movement(position, direction.move(position)));
            break;
          }
        }
      }
    }

    movements.stream()
        .collect(groupingBy(Movement::getTo))
        .values()
        .stream()
        .filter(movement -> movement.size() == 1)
        .map(movement -> movement.get(0))
        .forEach(movement -> {
          positions.remove(movement.from);
          positions.add(movement.to);
        });

    return !movements.isEmpty();
  }

  private boolean areAllAvailable(Collection<Position> neighbours) {
    for (var neighbour : neighbours) {
      if (positions.contains(neighbour)) {
        return false;
      }
    }
    return true;
  }

  private int countFreePositions() {
    int minRow = positions.stream().mapToInt(Position::getRow).min().orElseThrow();
    int maxRow = positions.stream().mapToInt(Position::getRow).max().orElseThrow();
    int minCol = positions.stream().mapToInt(Position::getCol).min().orElseThrow();
    int maxCol = positions.stream().mapToInt(Position::getCol).max().orElseThrow();

    int rows = abs(minRow - maxRow) + 1;
    int cols = abs(minCol - maxCol) + 1;

    int totalArea = rows * cols;
    return totalArea - positions.size();
  }

  private void resetDirections() {
    directions = new LinkedList<>(List.of(NORTH, SOUTH, WEST, EAST));
  }

  private void updateDirections() {
    directions.addLast(directions.removeFirst());
  }

  private void parsePositions(String input) {
    positions = new HashSet<>();
    final List<String> lines = getLines(input);
    for (int row = 0; row < lines.size(); row++) {
      var line = lines.get(row);
      for (int col = 0; col < line.length(); col++) {
        if (ELF.equals(line.charAt(col))) {
          positions.add(position(row, col));
        }
      }
    }
  }

  enum Direction {
    NORTH(List.of(position(-1, -1), position(-1, 0), position(-1, +1))),
    SOUTH(List.of(position(+1, -1), position(+1, 0), position(+1, +1))),
    WEST(List.of(position(-1, -1), position(0, -1), position(+1, -1))),
    EAST(List.of(position(-1, +1), position(0, +1), position(+1, +1)));

    private final List<Position> directionNeighbours;

    private final static Set<Position> allNeighbours = Stream
        .of(NORTH.directionNeighbours, SOUTH.directionNeighbours, WEST.directionNeighbours, EAST.directionNeighbours)
        .flatMap(Collection::stream)
        .collect(toSet());

    Direction(List<Position> directionNeighbours) {
      this.directionNeighbours = directionNeighbours;
    }

    public Collection<Position> directionNeighbours(Position position) {
      return directionNeighbours.stream()
          .map(neighbour -> neighbour.add(position))
          .collect(toList());
    }

    public static Collection<Position> allNeighbours(Position position) {
      return allNeighbours.stream()
          .map(neighbour -> neighbour.add(position))
          .collect(toList());
    }

    public Position move(Position position) {
      // it's always the middle position that's orthogonal
      return directionNeighbours.get(1).add(position);
    }
  }

  @Getter
  @AllArgsConstructor
  static class Movement {

    Position from, to;

    static Movement movement(Position from, Position to) {
      return new Movement(from, to);
    }
  }

  @Getter
  @EqualsAndHashCode
  @AllArgsConstructor
  static class Position {

    int row, col;

    static Position position(int row, int col) {
      return new Position(row, col);
    }

    Position add(Position position) {
      return position(position.row + this.row, position.col + this.col);
    }
  }
}