package com.radupetre.adventofcode.year2020.day20;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum Transform {
  UNCHANGED(false, 0),
  CLOCKWISE_ONCE(false, 1),
  CLOCKWISE_TWICE(false, 2),
  CLOCKWISE_THRICE(false, 3),
  FLIP(true, 0),
  FLIP_CLOCKWISE_ONCE(true, 1),
  FLIP_CLOCKWISE_TWICE(true, 2),
  FLIP_CLOCKWISE_THRICE(true, 3);

  final boolean isFlip;
  final int rotations;

  static Transform get(boolean isFlip, int rotations) {
    return Arrays.stream(Transform.values())
        .filter(transform -> isFlip == transform.isFlip)
        .filter(transform -> rotations == transform.rotations)
        .findFirst()
        .get();
  }
}
