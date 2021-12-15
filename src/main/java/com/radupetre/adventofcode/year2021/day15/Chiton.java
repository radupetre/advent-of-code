package com.radupetre.adventofcode.year2021.day15;

import static java.lang.Character.getNumericValue;
import static java.util.Arrays.fill;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.List;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/15
 */
@Service
@Log4j2
public class Chiton extends AbstractAdventSolution {

  private static final int[][] NEIGHBOUR_ADJUSTMENTS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

  int xMax;
  int yMax;
  int[][] riskLevels;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 15);
  }

  @Override
  public Object solvePart1(String input) {
    riskLevels = parseRiskLevels(input);
    return lowestRisk(riskLevels, xMax, yMax);
  }

  @Override
  public Object solvePart2(String input) {
    riskLevels = parseRiskLevels(input);
    riskLevels = expandRiskLevels(riskLevels, 5);
    return lowestRisk(riskLevels, xMax, yMax);
  }

  int lowestRisk(int[][] riskLevels, int xMax, int yMax) {
    // it's a sort of dijkstra
    int[][] lowestRisk = new int[xMax][yMax];
    stream(lowestRisk).forEach(line -> fill(line, Integer.MAX_VALUE));

    PriorityQueue<RiskPosition> positionQueue = new PriorityQueue<>(xMax * yMax, comparingInt(p -> p.riskLevel));

    // insert first position but don't count the risk for it
    lowestRisk[0][0] = 0;
    positionQueue.add(new RiskPosition(0, 0, lowestRisk[0][0]));

    while (!positionQueue.isEmpty()) {
      RiskPosition currentPos = positionQueue.poll();

      stream(NEIGHBOUR_ADJUSTMENTS)
          .map(adj -> new Coordinate(currentPos.x + adj[0], currentPos.y + adj[1]))
          .filter(coord -> coord.x >= 0 && coord.x < xMax && coord.y >= 0 && coord.y < yMax)
          .map(coord -> new RiskPosition(coord.x, coord.y, lowestRisk[coord.x][coord.y]))
          .filter(nextPos -> nextPos.riskLevel > currentPos.riskLevel + riskLevels[nextPos.x][nextPos.y])
          .map(nextPos -> nextPos.withRiskLevel(riskLevels[nextPos.x][nextPos.y] + lowestRisk[currentPos.x][currentPos.y]))
          .forEach(nextPos -> {
            lowestRisk[nextPos.x][nextPos.y] = nextPos.riskLevel;
            positionQueue.add(nextPos);
          });
    }

    return lowestRisk[xMax - 1][yMax - 1];
  }

  private int[][] expandRiskLevels(int[][] riskLevels, int expandFactor) {
    int expandedXMax = expandFactor * xMax;
    int expandedYMax = expandFactor * yMax;
    int[][] expandedRiskLevels = new int[expandedXMax][expandedYMax];

    for (int x = 0; x < expandedXMax; x++) {
      for (int y = 0; y < expandedYMax; y++) {
        int riskIncrease = x / xMax + y / yMax;
        int riskValue = riskLevels[x % xMax][y % yMax];
        expandedRiskLevels[x][y] = riskValue + riskIncrease;
        expandedRiskLevels[x][y] -= (expandedRiskLevels[x][y] > 9) ? 9 : 0; // after 9 loop back to 1
      }
    }

    xMax = expandedXMax;
    yMax = expandedYMax;
    return expandedRiskLevels;
  }

  private int[][] parseRiskLevels(String input) {
    final List<String> lines = StringUtility.getLines(input);

    xMax = lines.size();
    yMax = lines.get(0).length();
    int[][] riskLevels = new int[xMax][yMax];

    for (int x = 0; x < xMax; x++) {
      for (int y = 0; y < yMax; y++) {
        riskLevels[x][y] = getNumericValue(lines.get(x).charAt(y));
      }
    }
    return riskLevels;
  }
}

@RequiredArgsConstructor
class Coordinate {
  final int x, y;
}

@With
@RequiredArgsConstructor
class RiskPosition {
  final int x, y, riskLevel;
}