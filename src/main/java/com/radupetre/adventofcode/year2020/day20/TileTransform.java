package com.radupetre.adventofcode.year2020.day20;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TileTransform {

  final Tile tile;
  final Transform transform;

  static TileTransform of(Tile tile, Transform transform) {
    return new TileTransform(tile, transform);
  }

  public Integer getBorder(Side side) {
    return tile.getBorderAfterTransform(side, transform);
  }

  public boolean[][] getBoardSection() {
    return tile.getBoardSectionAfterTransform(transform);
  }
}
