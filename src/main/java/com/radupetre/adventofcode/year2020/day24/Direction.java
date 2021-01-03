package com.radupetre.adventofcode.year2020.day24;

enum Direction {
  E(1, 1, 0),
  SE(1, 0, -1),
  SW(0, -1, -1),
  W(-1, -1, 0),
  NW(-1, 0, 1),
  NE(0, 1, 1);

  final int x; //   \ axis
  final int y; //   / axis
  final int z; //   | axis

  Direction(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
