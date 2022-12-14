package com.radupetre.adventofcode.year2022.day14;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static com.radupetre.adventofcode.year2022.day14.RegolithReservoir.Coordinate.coordinate;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/14
 */
@Service
@Log4j2
public class RegolithReservoir extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 14);
  }

  Set<Coordinate> obstacles;
  int minX, maxX, minY, maxY;

  Coordinate moveDown = coordinate(0, 1);
  Coordinate moveDownLeft = coordinate(-1, 1);
  Coordinate moveDownRight = coordinate(1, 1);

  @Override
  public Object solvePart1(String input) {
    obstacles = parseRockCoordinates(input);
    updateMinMaxCoordinates();
    return getSandCount();
  }

  @Override
  public Object solvePart2(String input) {
    obstacles = parseRockCoordinates(input);
    updateMinMaxCoordinates();
    obstacles.addAll(getFloorCoordinates());
    return getSandCount();
  }

  private long getSandCount() {
    int sandCount = 0;
    while (true) {
      Coordinate current = coordinate(500, 0);
      if (obstacles.contains(current)) {
        return sandCount;
      }

      while (true) {
        if (!obstacles.contains(current.add(moveDown))) {
          current = current.add(moveDown);
        } else if (!obstacles.contains((current.add(moveDownLeft)))) {
          current = current.add(moveDownLeft);
        } else if (!obstacles.contains((current.add(moveDownRight)))) {
          current = current.add(moveDownRight);
        } else {
          obstacles.add(current);
          sandCount++;
          break;
        }

        if (current.x < minX || current.x > maxX || current.y < minY || current.y > maxY) {
          return sandCount;
        }
      }
    }
  }

  private List<Coordinate> getFloorCoordinates() {
    maxY += 2;
    minX -= 200;
    maxX += 200;
    return IntStream.range(minX, maxX)
        .mapToObj(x -> coordinate(x, maxY))
        .collect(toList());
  }

  private void updateMinMaxCoordinates() {
    var xCoordinates = obstacles.stream().map(coordinate -> coordinate.x).sorted().collect(toList());
    minX = xCoordinates.get(0);
    maxX = xCoordinates.get(xCoordinates.size() - 1);
    minY = 0;
    maxY = obstacles.stream().map(coordinate -> coordinate.y).sorted(reverseOrder()).findFirst().orElseThrow();
  }

  private Set<Coordinate> parseRockCoordinates(String input) {
    return getLinesAsStream(input)
        .flatMap(this::parseRockFormation)
        .collect(toSet());
  }

  private Stream<Coordinate> parseRockFormation(String input) {
    var coordinates = getBatchesAsStream(input, "->")
        .map(this::parseCoordinate)
        .collect(toList());

    return IntStream.range(0, coordinates.size() - 1)
        .boxed()
        .flatMap(i -> getCoordinatesBetween(coordinates.get(i), coordinates.get(i + 1)));
  }

  private Stream<Coordinate> getCoordinatesBetween(Coordinate c1, Coordinate c2) {
    if (c1.x == c2.x) {
      return IntStream.range(min(c1.y, c2.y), max(c1.y, c2.y) + 1).mapToObj(y -> coordinate(c1.x, y));
    } else {
      return IntStream.range(min(c1.x, c2.x), max(c1.x, c2.x) + 1).mapToObj(x -> coordinate(x, c1.y));
    }
  }

  private Coordinate parseCoordinate(String coordinate) {
    final String[] parts = coordinate.split(COMMA);
    return coordinate(parseInt(parts[0].trim()), parseInt(parts[1].trim()));
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class Coordinate {

    final int x, y;

    static Coordinate coordinate(int x, int y) {
      return new Coordinate(x, y);
    }

    Coordinate add(Coordinate coordinate) {
      return coordinate(coordinate.x + this.x, coordinate.y + this.y);
    }
  }
}