package com.radupetre.adventofcode.year2020.day17;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Cubes {

  final int sizeX;
  final int sizeY;
  final int sizeZ;
  final boolean[][][] active;

  Cubes getExpansion() {
    // get a expansion of the current cube in each direction
    return new Cubes(
        this.sizeX + 2,
        this.sizeY + 2,
        this.sizeZ + 2,
        new boolean[this.sizeX + 2][this.sizeY + 2][this.sizeZ + 2]
    );
  }
}
