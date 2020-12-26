package com.radupetre.adventofcode.year2020.day03;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.Arrays.asList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/3
 */
@Service
@Log4j2
public class TobogganTrajectory extends AbstractAdventSolution {

  private static final Character TREE_SYMBOL = '#';

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 3);
  }

  @Override
  public Object solvePart1(String map) {
    List<String> mapLines = getLines(map);
    return countTreesOnSlope(mapLines, 3, 1);
  }

  @Override
  public Object solvePart2(String map) {
    List<String> mapLines = getLines(map);
    return multiplyTreesOnAllSlopes(mapLines);
  }

  private int countTreesOnSlope(List<String> mapLines, int slopeColumns, int slopeRows) {
    int treeCount = 0;
    int currentCol = 0;
    int currentRow = 0;
    int maxColumn = mapLines.get(0).length();
    int maxRow = mapLines.size();

    do {
      String currentMapLine = mapLines.get(currentRow);
      if (TREE_SYMBOL.equals(currentMapLine.charAt(currentCol))) {
        treeCount++;
      }

      currentCol = (currentCol + slopeColumns) % maxColumn;
      currentRow += slopeRows;
    } while (currentRow < maxRow);

    return treeCount;
  }

  private long multiplyTreesOnAllSlopes(List<String> mapLines) {
    return Stream.of(
        countTreesOnSlope(mapLines, 1, 1),
        countTreesOnSlope(mapLines, 3, 1),
        countTreesOnSlope(mapLines, 5, 1),
        countTreesOnSlope(mapLines, 7, 1),
        countTreesOnSlope(mapLines, 1, 2))
        .mapToLong(Long::valueOf)
        .reduce((a, b) -> a * b)
        .getAsLong();
  }
}
