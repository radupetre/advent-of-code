package com.radupetre.adventofcode.year2021.day05;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/5
 */
@Service
@Log4j2
public class HydrothermalVenture extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 5);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .map(Line::new)
        .filter(not(Line::isDiagonal)) // for part 1 we only want horizontal/vertical lines
        .map(Line::getPoints)
        .flatMap(List::stream)
        .collect(groupingBy(identity(), counting()))
        .values()
        .stream()
        .filter(occurrence -> occurrence > 1)
        .count();
  }

  @Override
  public Object solvePart2(String input) {
    return getLinesAsStream(input)
        .map(Line::new)
        .map(Line::getPoints)
        .flatMap(List::stream)
        .collect(groupingBy(identity(), counting()))
        .values()
        .stream()
        .filter(occurrence -> occurrence > 1)
        .count();
  }
}

class Line {

  int x1, x2, y1, y2;

  Line(String coordinatesString) {
    final List<Integer> coordinates = stream(coordinatesString.split("->"))
        .map(coordinatePair -> coordinatePair.split(COMMA))
        .map(Arrays::asList)
        .flatMap(List::stream)
        .map(String::trim)
        .map(Integer::parseInt)
        .collect(toList());

    x1 = coordinates.get(0);
    y1 = coordinates.get(1);
    x2 = coordinates.get(2);
    y2 = coordinates.get(3);
  }

  public boolean isDiagonal() {
    return x1 != x2 && y1 != y2;
  }

  public List<Point> getPoints() {
    List<Point> linePoints = new ArrayList<>();
    int currentX = x1;
    int currentY = y1;
    boolean isLastPoint = false;

    // step is 0 for horizontal/vertical lines or -1 / +1 for sloped lines. Int compare delivers exactly that.
    int xStep = Integer.compare(x2, x1);
    int yStep = Integer.compare(y2, y1);

    while (true) {
      linePoints.add(new Point(currentX, currentY));

      if (isLastPoint) {
        break;
      }

      currentX += xStep;
      currentY += yStep;

      if (currentX == x2 && currentY == y2) {
        isLastPoint = true;
      }
    }

    return linePoints;
  }
}

@EqualsAndHashCode
@RequiredArgsConstructor
class Point {

  final int x, y;
}