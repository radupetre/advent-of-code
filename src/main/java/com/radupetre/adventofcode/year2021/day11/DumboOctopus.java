package com.radupetre.adventofcode.year2021.day11;

import static com.radupetre.adventofcode.utils.StringUtility.NEW_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static java.lang.Character.getNumericValue;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/11
 */
@Service
@Log4j2
public class DumboOctopus extends AbstractAdventSolution {

  // orthogonal and diagonal
  private static final int[][] NEIGHBOUR_ADJUSTMENTS = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1},
      {-1, -1}, {-1, 0}, {-1, 1}};

  private int maxL, maxC;
  private int[][] energyLevels;
  private boolean[][] hasFlashed;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 11);
  }

  @Override
  public Object solvePart1(String input) {
    energyLevels = parseEnergyLevels(input);

    return range(0, 100)
        .map(this::getFlashesDuringStep)
        .reduce(0, Integer::sum);
  }

  @Override
  public Object solvePart2(String input) {
    energyLevels = parseEnergyLevels(input);

    int stepCount = 1;
    while (getFlashesDuringStep(stepCount) != 100) {
      stepCount++;
    }

    return stepCount;
  }

  private int getFlashesDuringStep(int cycle) {
    // first increment all energy levels by 1
    for (int l = 0; l < maxL; l++) {
      for (int c = 0; c < maxC; c++) {
        energyLevels[l][c] += 1;
      }
    }

    // then have flashed positions increment neighbours
    hasFlashed = new boolean[maxL][maxC];
    int currentFlashCount = 0, newFlashCount = -1;
    // keep evaluating flashes if the last iteration resulted in new flashes
    while (newFlashCount != 0) {
      newFlashCount = 0;
      for (int l = 0; l < maxL; l++) {
        for (int c = 0; c < maxC; c++) {
          if (energyLevels[l][c] >= 10 && !hasFlashed[l][c]) {
            newFlashCount++;
            hasFlashed[l][c] = true;
            for (Coordinate neighbour : getNeighbours(l, c)) {
              energyLevels[neighbour.l][neighbour.c] = energyLevels[neighbour.l][neighbour.c] + 1;
            }
          }
        }
      }
      currentFlashCount += newFlashCount;
    }

    // then reset all flashed positions to 0 energy
    for (int l = 0; l < maxL; l++) {
      for (int c = 0; c < maxC; c++) {
        if (energyLevels[l][c] >= 10) {
          energyLevels[l][c] = 0;
        }
      }
    }

    return currentFlashCount;
  }

  private int[][] parseEnergyLevels(String input) {
    final List<String> lines = getBatches(input, NEW_LINE);
    maxL = lines.size();
    maxC = lines.get(0).length();

    int[][] energyLevels = new int[maxL][maxC];
    for (int l = 0; l < maxL; l++) {
      for (int c = 0; c < maxC; c++) {
        energyLevels[l][c] = getNumericValue(lines.get(l).charAt(c));
      }
    }
    return energyLevels;
  }

  private List<Coordinate> getNeighbours(int l, int c) {
    return Arrays.stream(NEIGHBOUR_ADJUSTMENTS)
        .map(adjustment -> new Coordinate(l + adjustment[0], c + adjustment[1]))
        .filter(neighbour ->
            neighbour.l >= 0
                && neighbour.c >= 0
                && neighbour.l < maxL
                && neighbour.c < maxC)
        .collect(toList());
  }
}

@RequiredArgsConstructor
class Coordinate {

  final int l, c;
}
