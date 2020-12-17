package com.radupetre.adventofcode.year2020.day17;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.Result;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/17
 */
@Service
@Log4j2
public class ConwayCubes extends AbstractAdventSolution {

  private final static char ACTIVE = '#';

  private final static int X_ADJUSTMENT = 0;
  private final static int Y_ADJUSTMENT = 1;
  private final static int Z_ADJUSTMENT = 2;

  private int[][] neighbourAdjustments = initNeighbourAdjustments();

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 17);
  }

  @Override
  public Result solve(String input) {
    Cubes startingCubes = parseCubes(input);

    long activeCubesAfterSixCycles = countActiveCubesAfterCycles(startingCubes, 6);
    log.info("Active cubes after 6 cycles: %s".formatted(activeCubesAfterSixCycles));

    return new Result(activeCubesAfterSixCycles, 0);
  }

  private long countActiveCubesAfterCycles(Cubes currentCubes, int cycles) {
    // execute the cycles then count the active;
    for (int cycle = 0; cycle < cycles; cycle++) {
      currentCubes = getNextCubesCycle(currentCubes);
    }

    return countActive(currentCubes);
  }

  private Cubes getNextCubesCycle(Cubes currentCubes) {
    Cubes nextCubes = currentCubes.getExpansion();

    for (int x = 0; x < nextCubes.sizeX; x++) {
      for (int y = 0; y < nextCubes.sizeY; y++) {
        for (int z = 0; z < nextCubes.sizeZ; z++) {
          // since nextCubes is an expansion, the desired position is at smaller coordinates
          int neighbourCount = countNeighbours(x - 1, y - 1, z - 1, currentCubes);
          boolean wasActive = isActive(x - 1, y - 1, z - 1, currentCubes);

          // active stays active if it has exactly 2 or 3 neighbours
          // inactive becomes active if it has exactly 3 neighbours
          boolean isActive = wasActive
              ? neighbourCount == 2 || neighbourCount == 3
              : neighbourCount == 3;

          nextCubes.active[x][y][z] = isActive;
        }
      }
    }

    return nextCubes;
  }

  private boolean isActive(int x, int y, int z, Cubes cubes) {
    return 0 <= x && x < cubes.sizeX
        && 0 <= y && y < cubes.sizeY
        && 0 <= z && z < cubes.sizeZ
        && cubes.active[x][y][z];
  }

  private int countNeighbours(int x, int y, int z, Cubes cubes) {
    return (int) Arrays.stream(neighbourAdjustments)
        .filter(neighbourAdjustment -> isActive(
            x + neighbourAdjustment[X_ADJUSTMENT],
            y + neighbourAdjustment[Y_ADJUSTMENT],
            z + neighbourAdjustment[Z_ADJUSTMENT],
            cubes)
        ).count();
  }

  private long countActive(Cubes currentCubes) {
    return Arrays.stream(currentCubes.active)
        .flatMap(Arrays::stream)
        .mapToLong(this::countActive)
        .sum();
  }

  private long countActive(boolean[] active) {
    return IntStream.range(0, active.length)
        .mapToObj(position -> active[position])
        .filter(isActive -> isActive)
        .count();
  }

  private Cubes parseCubes(String input) {
    final List<String> lines = getLines(input);

    int sizeX = lines.get(0).length();
    int sizeY = lines.size();
    int sizeZ = 1;
    boolean[][][] active = new boolean[sizeX][sizeY][sizeZ];

    // read the provided layer
    for (int x = 0; x < lines.size(); x++) {
      String line = lines.get(x);
      for (int y = 0; y < line.length(); y++) {
        char state = line.charAt(y);
        active[x][y][0] = ACTIVE == state;
      }
    }

    return new Cubes(sizeX, sizeY, sizeZ, active);
  }

  private int[][] initNeighbourAdjustments() {
    int[][] neighbourAdjustments = new int[26][3];
    int neighbourCount = 0;

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {

          // exclude the origin x=0, y=0, z=0
          if (x != 0 || y != 0 || z != 0) {
            neighbourAdjustments[neighbourCount][X_ADJUSTMENT] = x;
            neighbourAdjustments[neighbourCount][Y_ADJUSTMENT] = y;
            neighbourAdjustments[neighbourCount][Z_ADJUSTMENT] = z;
            neighbourCount++;
          }
        }
      }
    }

    return neighbourAdjustments;
  }

}

