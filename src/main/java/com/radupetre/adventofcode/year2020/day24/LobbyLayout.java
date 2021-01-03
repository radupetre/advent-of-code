package com.radupetre.adventofcode.year2020.day24;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2020.day24.Direction.E;
import static com.radupetre.adventofcode.year2020.day24.Direction.NE;
import static com.radupetre.adventofcode.year2020.day24.Direction.NW;
import static com.radupetre.adventofcode.year2020.day24.Direction.SE;
import static com.radupetre.adventofcode.year2020.day24.Direction.SW;
import static com.radupetre.adventofcode.year2020.day24.Direction.W;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/23
 */
@Service
@Log4j2
public class LobbyLayout extends AbstractAdventSolution {

  private static final char DIRECTION_E = 'e';
  private static final char DIRECTION_S = 's';
  private static final char DIRECTION_W = 'w';
  private static final char DIRECTION_N = 'n';

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 24);
  }

  @Override
  public Object solvePart1(String instructionLines) {
    return getBlackTiles(instructionLines).size();
  }

  private Set<Coordinate> getBlackTiles(String instructionLines) {
    final List<Coordinate> endingCoordinates =
        getLines(instructionLines)
            .stream()
            .map(this::parseInstructionSet)
            .map(this::getEndingCoordinate)
            .collect(toList());

    // tiles are black after odd number of flips
    return endingCoordinates.stream()
        .collect(groupingBy(identity(), counting()))
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() % 2 == 1)
        .map(Entry::getKey)
        .collect(toSet());
  }

  private Coordinate getEndingCoordinate(List<Direction> directions) {
    Coordinate currentCoordinate = new Coordinate();

    for (Direction direction : directions) {
      currentCoordinate.x += direction.x;
      currentCoordinate.y += direction.y;
      currentCoordinate.z += direction.z;
    }
    return currentCoordinate;
  }

  private List<Direction> parseInstructionSet(String instructionLine) {
    List<Direction> directions = new ArrayList<>();

    for (int position = 0; position < instructionLine.length(); position++) {
      char primaryDirection = instructionLine.charAt(position);
      Direction currentDirection;

      if (isSimpleDirection(primaryDirection)) {
        currentDirection = getSimpleDirection(primaryDirection);
      } else {
        char secondaryDirection = instructionLine.charAt(++position);
        currentDirection = getComplexDirection(primaryDirection, secondaryDirection);
      }

      directions.add(currentDirection);
    }
    return directions;
  }

  private boolean isSimpleDirection(char direction) {
    return DIRECTION_E == direction || DIRECTION_W == direction;
  }

  private Direction getSimpleDirection(char direction) {
    return DIRECTION_E == direction ? E : W;
  }

  private Direction getComplexDirection(char primaryDirection, char secondaryDirection) {
    if (DIRECTION_N == primaryDirection) {
      return DIRECTION_E == secondaryDirection ? NE : NW;
    } else {
      return DIRECTION_E == secondaryDirection ? SE : SW;
    }
  }

  @Override
  public Object solvePart2(String instructionLines) {
    final Set<Coordinate> blackTiles = getBlackTiles(instructionLines);

    for (int times = 0; times < 100; times++) {
      Set<Coordinate> whiteTiles = getChangeableWhiteTiles(blackTiles);

      Set<Coordinate> newBlackTiles = getNewBlackTiles(whiteTiles, blackTiles);
      Set<Coordinate> unchangedBlackTiles = getUnchangedBlackTiles(blackTiles);

      blackTiles.clear();
      blackTiles.addAll(newBlackTiles);
      blackTiles.addAll(unchangedBlackTiles);
    }

    return blackTiles.size();
  }

  private long getBlackTileNeighbourCount(Coordinate tile, Set<Coordinate> blackTiles) {
    return getNeighbours(tile)
        .stream()
        .filter(blackTiles::contains)
        .count();
  }

  private Set<Coordinate> getChangeableWhiteTiles(Set<Coordinate> blackTiles) {
    final Set<Coordinate> changeableTiles = blackTiles.stream()
        .map(this::getNeighbours)
        .flatMap(Collection::stream)
        .collect(toSet());

    // keep only the white neighbouring tiles
    changeableTiles.removeAll(blackTiles);

    return changeableTiles;
  }

  private Set<Coordinate> getNeighbours(Coordinate tile) {
    return stream(Direction.values())
        .map(direction -> new Coordinate(
            tile.x + direction.x,
            tile.y + direction.y,
            tile.z + direction.z
        )).collect(toSet());
  }

  private Set<Coordinate> getNewBlackTiles(Set<Coordinate> whiteTiles, Set<Coordinate> blackTiles) {
    // a white tile with exactly 2 black tile neighbours becomes a black tile
    return whiteTiles.stream()
        .filter(whiteTile -> getBlackTileNeighbourCount(whiteTile, blackTiles) == 2L)
        .collect(toSet());
  }

  private Set<Coordinate> getUnchangedBlackTiles(Set<Coordinate> blackTiles) {
    // a black tile with 1 or 2 black tile neighbours stays a black tile
    return blackTiles.stream()
        .filter(blackTile -> {
          long blackTileNeighbourCount = getBlackTileNeighbourCount(blackTile, blackTiles);
          return blackTileNeighbourCount == 1 || blackTileNeighbourCount == 2;
        })
        .collect(toSet());
  }
}
