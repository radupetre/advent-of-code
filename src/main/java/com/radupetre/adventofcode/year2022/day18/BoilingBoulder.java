package com.radupetre.adventofcode.year2022.day18;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/18
 */
@Service
@Log4j2
public class BoilingBoulder extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 18);
  }

  Set<Cube> cubes;
  Cube minCorner;
  Cube maxCorner;

  @Override
  public Object solvePart1(String input) {
    parseCubes(input);
    return countExposedSides();
  }

  @Override
  public Object solvePart2(String input) {
    parseCubes(input);
    var outerArea_plus_innerArea = countExposedSides();

    calculateMinMaxCorners();
    fillMinMaxCuboid();

    var outerCuboidArea_plus_innerArea = countExposedSides();
    var outerCuboidArea = calculateSidesOfMinMaxCuboid();

    var innerArea = outerCuboidArea_plus_innerArea - outerCuboidArea;
    var outerArea = outerArea_plus_innerArea - innerArea;
    return outerArea;
  }

  private int calculateSidesOfMinMaxCuboid() {
    int x = maxCorner.x - minCorner.x + 1;
    int y = maxCorner.y - minCorner.y + 1;
    int z = maxCorner.z - minCorner.z + 1;
    return 2 * (x * y + y * z + x * z);
  }

  private void calculateMinMaxCorners() {
    minCorner = new Cube(
        cubes.stream().mapToInt(Cube::getX).min().orElseThrow() - 1,
        cubes.stream().mapToInt(Cube::getY).min().orElseThrow() - 1,
        cubes.stream().mapToInt(Cube::getZ).min().orElseThrow() - 1
    );
    maxCorner = new Cube(
        cubes.stream().mapToInt(Cube::getX).max().orElseThrow() + 1,
        cubes.stream().mapToInt(Cube::getY).max().orElseThrow() + 1,
        cubes.stream().mapToInt(Cube::getZ).max().orElseThrow() + 1
    );
  }

  private void fillMinMaxCuboid() {
    Stack<Cube> toFill = new Stack<>();
    toFill.add(minCorner);

    while (!toFill.isEmpty()) {
      Cube filled = toFill.pop();
      cubes.add(filled);
      for (var neighbour : filled.neighbours()) {
        if (!cubes.contains(neighbour) && isInMinMaxCuboid(neighbour)) {
          toFill.push(neighbour);
        }
      }
    }
  }

  private boolean isInMinMaxCuboid(Cube neighbour) {
    return minCorner.x <= neighbour.x && minCorner.y <= neighbour.y && minCorner.z <= neighbour.z &&
        neighbour.x <= maxCorner.x && neighbour.y <= maxCorner.y && neighbour.z <= maxCorner.z;
  }

  private int countExposedSides() {
    int exposedSides = cubes.size() * 6;
    for (var cube1 : cubes) {
      for (var cube2 : cubes) {
        if (abs(cube1.x - cube2.x) + abs(cube1.y - cube2.y) + abs(cube1.z - cube2.z) == 1) {
          exposedSides--;
        }
      }
    }
    return exposedSides;
  }

  private void parseCubes(String input) {
    cubes = getLinesAsStream(input)
        .map(line -> line.split(COMMA))
        .map(Cube::cube)
        .collect(Collectors.toSet());
  }

  @Getter
  @EqualsAndHashCode
  @AllArgsConstructor
  static class Cube {

    int x, y, z;

    static Cube cube(String[] coords) {
      return new Cube(parseInt(coords[0]), parseInt(coords[1]), parseInt(coords[2]));
    }

    Cube add(int x, int y, int z) {
      return new Cube(this.x + x, this.y + y, this.z + z);
    }

    Collection<Cube> neighbours() {
      return List.of(add(+1, 0, 0), add(-1, 0, 0), add(0, -1, 0),
          add(0, +1, 0), add(0, 0, -1), add(0, 0, +1)
      );
    }
  }
}