package com.radupetre.adventofcode.year2020.day20;

public class BoardUtils {

  static boolean[][] rotateClockwise(boolean[][] olbBoard, int sizeX, int sizeY) {
    boolean[][] newBoard = new boolean[sizeX][sizeY];

    for (int x = 0; x < sizeX; x++) {
      for (int y = 0; y < sizeY; y++) {
        newBoard[x][y] = olbBoard[sizeY - y - 1][x];
      }
    }

    return newBoard;
  }

  static boolean[][] flipHorizontal(boolean[][] oldBoard, int sizeX, int sizeY) {
    boolean[][] newBoard = new boolean[sizeX][sizeY];

    for (int x = 0; x < sizeX; x++) {
      for (int y = 0; y < sizeY; y++) {
        newBoard[x][y] = oldBoard[x][sizeY - y - 1];
      }
    }

    return newBoard;
  }
}
