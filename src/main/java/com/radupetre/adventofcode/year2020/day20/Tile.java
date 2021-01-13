package com.radupetre.adventofcode.year2020.day20;

import static com.radupetre.adventofcode.year2020.day20.BoardUtils.flipHorizontal;
import static com.radupetre.adventofcode.year2020.day20.BoardUtils.rotateClockwise;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class Tile {

  @EqualsAndHashCode.Include
  final Integer tileId;

  final String tileString;
  final boolean[][] tileCenter;
  final Set<Integer> borderValues;
  private final List<Integer> clockwiseBorders;
  private final List<Integer> reverseBorders;

  public Integer getBorderAfterTransform(Side side, Transform transform) {
    Integer borderPosition = transform.isFlip
        ? (transform.rotations - side.position + 4) % 4
        : (side.position - transform.rotations + 4) % 4;

    return transform.isFlip == side.isClockwise
        ? reverseBorders.get(borderPosition)
        : clockwiseBorders.get(borderPosition);
  }

  public Transform getTransformMatchingBorder(Side side, Integer value) {
    boolean isReverse = reverseBorders.contains(value);
    List<Integer> borders = isReverse ? reverseBorders : clockwiseBorders;

    final int transformPosition = IntStream.range(0, 4)
        .filter(borderPosition -> borders.get(borderPosition).equals(value))
        .findFirst()
        .getAsInt();

    boolean isFlip = side.isClockwise == isReverse;

    int rotations = isFlip
        ? (transformPosition + side.position + 4) % 4
        : (side.position - transformPosition + 4) % 4;

    return Transform.get(isFlip, rotations);
  }

  public boolean[][] getBoardSectionAfterTransform(Transform transform) {
    boolean[][] boardSection = tileCenter;

    if (transform.isFlip) {
      boardSection = flipHorizontal(boardSection, 8, 8);
    }

    for (int rotationCount = 0; rotationCount < transform.rotations; rotationCount++) {
      boardSection = rotateClockwise(boardSection, 8, 8);
    }

    return boardSection;
  }
}
