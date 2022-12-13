package com.radupetre.adventofcode.year2022.day12;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2022.day12.HillClimbingAlgorithm.Point.newPoint;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/12
 */
@Service
@Log4j2
public class HillClimbingAlgorithm extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 12);
  }

  private static final List<Point> movements = List.of(
      newPoint(-1, 0),
      newPoint(0, -1),
      newPoint(1, 0),
      newPoint(0, 1));
  private Point start, end;
  private int rows, cols;
  private Deque<Point> neighbours;
  private Map<Point, Character> lettersByPoint;
  private Map<Point, Integer> stepsByPoint;

  @Override
  public Object solvePart1(String input) {
    parseMap(input);
    return stepsFromPosition(start);
  }

  @Override
  public Object solvePart2(String input) {
    parseMap(input);
    return lettersByPoint.entrySet()
        .stream()
        .filter(pointAndLetter -> pointAndLetter.getValue() == 'a')
        .map(Entry::getKey)
        .mapToInt(this::stepsFromPosition)
        .filter(steps -> steps > 0)
        .min()
        .orElseThrow();
  }

  private int stepsFromPosition(Point start) {
    climbFromPosition(start);
    return stepsByPoint.getOrDefault(end, 0);
  }

  private void climbFromPosition(Point start) {
    stepsByPoint = new HashMap<>();
    neighbours = new LinkedList<>();
    neighbours.addLast(start);

    while (!neighbours.isEmpty()) {
      visitNeighbours(neighbours.removeFirst());
    }
  }

  private void parseMap(String input) {
    var lines = getLines(input);
    rows = lines.size();
    cols = lines.get(0).length();
    lettersByPoint = new HashMap<>();
    parseLines(lines);
  }

  private void parseLines(List<String> lines) {
    for (var row = 0; row < rows; row++) {
      for (var col = 0; col < cols; col++) {
        var letter = lines.get(row).charAt(col);
        var point = newPoint(row, col);

        if ('S' == letter) {
          start = point;
          letter = 'a';
        }

        if ('E' == letter) {
          end = point;
          letter = 'z';
        }

        lettersByPoint.put(point, letter);
      }
    }
  }

  private void visitNeighbours(Point from) {
    for (var movement : movements) {
      var to = from.add(movement);
      if (isInBounds(to) && isAccessible(from, to)) {
        var oldStepCount = stepsByPoint.getOrDefault(to, 0);
        var newStepCount = stepsByPoint.getOrDefault(from, 0) + 1;
        if (oldStepCount == 0 || newStepCount < oldStepCount) {
          stepsByPoint.put(to, newStepCount);
          neighbours.addLast(to);
        }
      }
    }
  }

  private boolean isAccessible(Point from, Point to) {
    return lettersByPoint.get(to) - 1 <= lettersByPoint.get(from);
  }

  private boolean isInBounds(Point point) {
    return (0 <= point.row && point.row < rows && 0 <= point.col && point.col < cols);
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class Point {

    final int row;
    final int col;

    static Point newPoint(int row, int col) {
      return new Point(row, col);
    }

    Point add(Point point) {
      return new Point(point.row + this.row, point.col + this.col);
    }
  }
}