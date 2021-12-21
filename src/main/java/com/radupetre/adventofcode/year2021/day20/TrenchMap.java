package com.radupetre.adventofcode.year2021.day20;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/20
 */
@Service
@Log4j2
public class TrenchMap extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 20);
  }

  private final int[][] NEIGHBOUR_ADJUSTMENTS = new int[][]{
      {-1, -1}, {-1, 0}, {-1, 1},
      {0, -1}, {0, 0}, {0, 1},
      {1, -1}, {1, 0}, {1, 1},
  };

  private int xMin, yMin, xMax, yMax;
  private Set<Point> lightPixels;

  @Override
  public Object solvePart1(String input) {
    final List<String> inputBatches = getBatches(input, StringUtility.EMPTY_LINE);
    String algorithm = inputBatches.get(0).trim();
    lightPixels = parseLightPixels(inputBatches.get(1));

    enhancePixels(algorithm, 2);
    return countLightPixels();
  }

  @Override
  public Object solvePart2(String input) {
    xMin = 0;
    yMin = 0;
    final List<String> inputBatches = getBatches(input, StringUtility.EMPTY_LINE);
    String algorithm = inputBatches.get(0).trim();
    lightPixels = parseLightPixels(inputBatches.get(1));

    enhancePixels(algorithm, 50);
    return countLightPixels();
  }

  private void enhancePixels(String algorithm, int steps) {
    // for n steps, adjusting the grid + (n * 2) to not worry about calculations at border/infinity
    adjustGrid(steps * 2);
    range(0, steps).forEach(step -> updatePixels(algorithm));
    // then shrink it back - n to remove border/infinity noise
    adjustGrid(-steps);
  }

  private void adjustGrid(int positions) {
    xMin -= positions;
    xMax += positions;
    yMin -= positions;
    yMax += positions;
  }

  private void updatePixels(String algo) {
    Set<Point> newPixels = new HashSet<>();
    for (int x = xMin; x < xMax; x++) {
      for (int y = yMin; y < yMax; y++) {
        final String binary = getNeighbours(x, y).stream()
            .map(lightPixels::contains)
            .map(isLight -> isLight ? "1" : "0")
            .collect(joining());
        int value = parseInt(binary, 2);
        if (algo.charAt(value) == '#') {
          newPixels.add(new Point(x, y));
        }
      }
    }
    lightPixels = newPixels;
  }

  private List<Point> getNeighbours(int x, int y) {
    return stream(NEIGHBOUR_ADJUSTMENTS)
        .map(adjustment -> new Point(x + adjustment[0], y + adjustment[1]))
        .collect(toList());
  }

  private int countLightPixels() {
    int count = 0;
    for (int x = xMin; x < xMax; x++) {
      for (int y = yMin; y < yMax; y++) {
        if (lightPixels.contains(new Point(x, y))) {
          count++;
        }
      }
    }
    return count;
  }

  private Set<Point> parseLightPixels(String input) {
    final List<String> lines = StringUtility.getLines(input);
    Set<Point> lightPixels = new HashSet<>();

    xMin = 0;
    yMin = 0;
    xMax = lines.size();
    yMax = lines.get(0).length();

    for (int x = xMin; x < xMax; x++) {
      for (int y = yMin; y < yMax; y++) {
        if (lines.get(x).charAt(y) == '#') {
          lightPixels.add(new Point(x, y));
        }
      }
    }
    return lightPixels;
  }
}

@EqualsAndHashCode
@AllArgsConstructor
class Point {

  int x, y;
}