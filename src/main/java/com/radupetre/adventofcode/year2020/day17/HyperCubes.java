package com.radupetre.adventofcode.year2020.day17;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class HyperCubes {

  private final static char ACTIVE = '#';
  private final static int X_ADJUSTMENT = 0;
  private final static int Y_ADJUSTMENT = 1;
  private final static int Z_ADJUSTMENT = 2;
  private final static int W_ADJUSTMENT = 3;
  private final int[][] neighbourAdjustments = initNeighbourAdjustments();

  final int sizeX;
  final int sizeY;
  final int sizeZ;
  final int sizeW;
  final boolean[][][][] active;

  private HyperCubes getExpansion() {
    // get a expansion of the current hyper cube in each direction
    return new HyperCubes(
        this.sizeX + 2,
        this.sizeY + 2,
        this.sizeZ + 2,
        this.sizeW + 2,
        new boolean[this.sizeX + 2][this.sizeY + 2][this.sizeZ + 2][this.sizeW + 2]
    );
  }

  HyperCubes getNextCubesCycle() {
    HyperCubes nextHyperCubes = getExpansion();

    for (int x = 0; x < nextHyperCubes.sizeX; x++) {
      for (int y = 0; y < nextHyperCubes.sizeY; y++) {
        for (int z = 0; z < nextHyperCubes.sizeZ; z++) {
          for (int w = 0; w < nextHyperCubes.sizeW; w++) {
            // since nextHyperCubes is an expansion, the desired position is at smaller coordinates
            int neighbourCount = countNeighbours(x - 1, y - 1, z - 1, w - 1);
            boolean wasActive = isActive(x - 1, y - 1, z - 1, w - 1);

            // active stays active if it has exactly 2 or 3 neighbours
            // inactive becomes active if it has exactly 3 neighbours
            boolean isActive = wasActive
                ? neighbourCount == 2 || neighbourCount == 3
                : neighbourCount == 3;

            nextHyperCubes.active[x][y][z][w] = isActive;
          }
        }
      }
    }
    return nextHyperCubes;
  }

  private boolean isActive(int x, int y, int z, int w) {
    return 0 <= x && x < sizeX
        && 0 <= y && y < sizeY
        && 0 <= z && z < sizeZ
        && 0 <= w && w < sizeW
        && active[x][y][z][w];
  }

  private int countNeighbours(int x, int y, int z, int w) {
    return (int) Arrays.stream(neighbourAdjustments)
        .filter(neighbourAdjustment -> isActive(
            x + neighbourAdjustment[X_ADJUSTMENT],
            y + neighbourAdjustment[Y_ADJUSTMENT],
            z + neighbourAdjustment[Z_ADJUSTMENT],
            w + neighbourAdjustment[W_ADJUSTMENT])
        ).count();
  }

  static HyperCubes parseHyperCubes(String input) {
    final List<String> lines = getLines(input);

    int sizeX = lines.get(0).length();
    int sizeY = lines.size();
    int sizeZ = 1;
    int sizeW = 1;
    boolean[][][][] active = new boolean[sizeX][sizeY][sizeZ][sizeW];

    // read the provided layer
    for (int x = 0; x < lines.size(); x++) {
      String line = lines.get(x);
      for (int y = 0; y < line.length(); y++) {
        char state = line.charAt(y);
        active[x][y][0][0] = ACTIVE == state;
      }
    }

    return new HyperCubes(sizeX, sizeY, sizeZ, sizeW, active);
  }

  private int[][] initNeighbourAdjustments() {
    int[][] neighbourAdjustments = new int[80][4];
    int neighbourCount = 0;

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          for (int w = -1; w <= 1; w++) {
            // exclude the origin x=0, y=0, z=0, w=0
            if (x != 0 || y != 0 || z != 0 || w != 0) {
              neighbourAdjustments[neighbourCount][X_ADJUSTMENT] = x;
              neighbourAdjustments[neighbourCount][Y_ADJUSTMENT] = y;
              neighbourAdjustments[neighbourCount][Z_ADJUSTMENT] = z;
              neighbourAdjustments[neighbourCount][W_ADJUSTMENT] = w;
              neighbourCount++;
            }
          }
        }
      }
    }
    return neighbourAdjustments;
  }
}
