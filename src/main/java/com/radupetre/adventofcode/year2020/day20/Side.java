package com.radupetre.adventofcode.year2020.day20;

import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum Side {
  TOP(0, true, 0, -1),
  RIGHT(1, true, 1, 0),
  BOTTOM(2, false, 0, 1),
  LEFT(3, false, -1, 0);

  final int position;
  final boolean isClockwise;
  final int xPosition;
  final int yPosition;


  private static final Map<Side, Side> oppositeSides = Map.of(
      TOP, BOTTOM,
      BOTTOM, TOP,
      LEFT, RIGHT,
      RIGHT, LEFT
  );

  public static Side byPosition(int position) {
    return Arrays.stream(Side.values())
        .filter(side -> side.position == position)
        .findFirst()
        .get();
  }

  public Side getOpposite() {
    return oppositeSides.get(this);
  }
}
