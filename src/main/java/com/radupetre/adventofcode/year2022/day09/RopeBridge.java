package com.radupetre.adventofcode.year2022.day09;

import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Math.abs;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashSet;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/9
 */
@Service
@Log4j2
public class RopeBridge extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 9);
  }

  HashSet<String> tailKnotCoordinates = null;
  int knotRows[] = null;
  int knotCols[] = null;

  @Override
  public Object solvePart1(String input) {
    doMovements(input, 2);
    return tailKnotCoordinates.size();
  }

  @Override
  public Object solvePart2(String input) {
    doMovements(input, 10);
    return tailKnotCoordinates.size();
  }

  private void doMovements(String input, int knotCount) {
    tailKnotCoordinates = new HashSet<>();
    knotRows = new int[knotCount];
    knotCols = new int[knotCount];
    tailKnotCoordinates.add(" " + knotRows[knotCount - 1] + " " + knotCols[knotCount - 1]);

    var lines = getLines(input);
    for (var line : lines) {
      final String[] words = line.trim().split(SPACE);
      var direction = words[0];
      var distance = Integer.parseInt(words[1]);

      int adjRow = 0;
      int adjCol = 0;
      switch (direction) {
        case "R" -> adjCol++;
        case "U" -> adjRow++;
        case "L" -> adjCol--;
        case "D" -> adjRow--;
        default -> throw new IllegalStateException();
      }
      ;

      for (int times = 0; times < distance; times++) {
        moveKnotsInDirection(adjRow, adjCol, knotCount);
        tailKnotCoordinates.add(" " + knotRows[knotCount - 1] + " " + knotCols[knotCount - 1]);
      }
    }
  }

  private void moveKnotsInDirection(int adjRow, int adjCol, int knotCount) {
    for (int knot = 0; knot < knotCount; knot++) {
      if (knot == 0) {
        // move the head
        knotRows[0] += adjRow;
        knotCols[0] += adjCol;
      } else {
        int deltaCol = knotCols[knot - 1] - knotCols[knot];
        int deltaRow = knotRows[knot - 1] - knotRows[knot];

        if (abs(deltaCol) <= 1 && abs(deltaRow) <= 1) {
          continue; // knots are touching
        }

        int moveTolerance = onSameRowOrCol(knot, knot - 1) ? 1 : 0;
        if (abs(deltaCol) > moveTolerance) {
          if (deltaCol > 0) {
            knotCols[knot]++;
          } else {
            knotCols[knot]--;
          }
        }
        if (abs(deltaRow) > moveTolerance) {
          if (deltaRow > 0) {
            knotRows[knot]++;
          } else {
            knotRows[knot]--;
          }
        }
      }
    }

  }

  private boolean onSameRowOrCol(int current, int previous) {
    return knotRows[current] == knotRows[previous] || knotCols[current] == knotCols[previous];
  }
}
