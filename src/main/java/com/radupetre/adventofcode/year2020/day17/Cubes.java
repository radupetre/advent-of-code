package com.radupetre.adventofcode.year2020.day17;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Cubes {

  private final static char ACTIVE = '#';
  private final static int X_ADJUSTMENT = 0;
  private final static int Y_ADJUSTMENT = 1;
  private final static int Z_ADJUSTMENT = 2;
  private final int[][] neighbourAdjustments = initNeighbourAdjustments();

  final int sizeX;
  final int sizeY;
  final int sizeZ;
  final boolean[][][] active;

  private Cubes getExpansion() {
    // get a expansion of the current cube in each direction
    return new Cubes(
        this.sizeX + 2,
        this.sizeY + 2,
        this.sizeZ + 2,
        new boolean[this.sizeX + 2][this.sizeY + 2][this.sizeZ + 2]
    );
  }

  Cubes getNextCubesCycle() {
    Cubes nextCubes = getExpansion();

    for (int x = 0; x < nextCubes.sizeX; x++) {
      for (int y = 0; y < nextCubes.sizeY; y++) {
        for (int z = 0; z < nextCubes.sizeZ; z++) {
          // since nextCubes is an expansion, the desired position is at smaller coordinates
          int neighbourCount = countNeighbours(x - 1, y - 1, z - 1);
          boolean wasActive = isActive(x - 1, y - 1, z - 1);

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

  private boolean isActive(int x, int y, int z) {
    return 0 <= x && x < sizeX
        && 0 <= y && y < sizeY
        && 0 <= z && z < sizeZ
        && active[x][y][z];
  }

  private int countNeighbours(int x, int y, int z) {
    return (int) Arrays.stream(neighbourAdjustments)
        .filter(neighbourAdjustment -> isActive(
            x + neighbourAdjustment[X_ADJUSTMENT],
            y + neighbourAdjustment[Y_ADJUSTMENT],
            z + neighbourAdjustment[Z_ADJUSTMENT])
        ).count();
  }

  static Cubes parseCubes(String input) {
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
