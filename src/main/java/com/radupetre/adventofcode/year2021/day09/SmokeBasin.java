package com.radupetre.adventofcode.year2021.day09;

import static com.radupetre.adventofcode.utils.StringUtility.NEW_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static java.lang.Character.getNumericValue;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/9
 */
@Service
@Log4j2
public class SmokeBasin extends AbstractAdventSolution {

  private static final int[][] NEIGHBOUR_ADJUSTMENTS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

  private int maxL, maxC;
  private int[][] heights;
  private boolean[][] visited;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 9);
  }

  @Override
  public Object solvePart1(String input) {
    heights = parseHeights(input);
    maxL = heights.length;
    maxC = heights[0].length;

    return getLowPoints().stream()
        .map(lowPoint -> heights[lowPoint.l][lowPoint.c] + 1)
        .reduce(0, Integer::sum);
  }

  @Override
  public Object solvePart2(String input) {
    heights = parseHeights(input);
    maxL = heights.length;
    maxC = heights[0].length;
    visited = new boolean[maxL][maxC];

    // mark high points as visited/unpassable
    getHighPoints()
        .forEach(highPoint -> visited[highPoint.l][highPoint.c] = true);

    // get all areas starting from low points
    return getLowPoints().stream()
        .map(this::calculateArea)
        .sorted(reverseOrder())
        .limit(3)
        .reduce(1, (a, b) -> a * b);
  }

  private List<Coordinate> getLowPoints() {
    List<Coordinate> lowPoints = new ArrayList<>();
    for (int l = 0; l < maxL; l++) {
      for (int c = 0; c < maxC; c++) {
        if (isLowPoint(l, c)) {
          lowPoints.add(new Coordinate(l, c));
        }
      }
    }
    return lowPoints;
  }

  private boolean isLowPoint(int l, int c) {
    return getNeighbours(l, c).stream()
        .map(neighbour -> heights[neighbour.l][neighbour.c])
        .filter(neighbourValue -> neighbourValue <= heights[l][c])
        .findFirst()
        .isEmpty();
  }

  private List<Coordinate> getNeighbours(int l, int c) {
    return Arrays.stream(NEIGHBOUR_ADJUSTMENTS)
        .map(adjustment -> new Coordinate(l + adjustment[0], c + adjustment[1]))
        .filter(neighbour ->
            neighbour.l >= 0
                && neighbour.c >= 0
                && neighbour.l <= maxL - 1
                && neighbour.c <= maxC - 1)
        .collect(toList());
  }

  private List<Coordinate> getHighPoints() {
    List<Coordinate> highPoints = new ArrayList<>();
    for (int l = 0; l < maxL; l++) {
      for (int c = 0; c < maxC; c++) {
        if (heights[l][c] == 9) {
          highPoints.add(new Coordinate(l, c));
        }
      }
    }
    return highPoints;
  }

  private int calculateArea(Coordinate current) {
    visited[current.l][current.c] = true;

    return getNeighbours(current.l, current.c)
        .stream()
        .filter(neighbour -> !visited[neighbour.l][neighbour.c])
        .map(this::calculateArea)
        .reduce(0, Integer::sum) + 1; // add 1 for the coordinate that we've just visited
  }

  private int[][] parseHeights(String input) {
    final List<String> lines = getBatches(input, NEW_LINE);
    int maxL = lines.size();
    int maxC = lines.get(0).length();

    int[][] heights = new int[maxL][maxC];
    for (int l = 0; l < maxL; l++) {
      for (int c = 0; c < maxC; c++) {
        heights[l][c] = getNumericValue(lines.get(l).charAt(c));
      }
    }
    return heights;
  }
}

@RequiredArgsConstructor
class Coordinate {
  final int l, c;
}
