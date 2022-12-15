package com.radupetre.adventofcode.year2022.day15;

import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.utils.StringUtility.keepDigits;
import static com.radupetre.adventofcode.year2022.day15.BeaconExclusionZone.Position.position;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/15
 */
@Service
@Log4j2
public class BeaconExclusionZone extends AbstractAdventSolution {

  List<Position> sensors, beacons;
  Set<Position> isBeacon;
  List<Integer> distances;
  int minX, maxX;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 15);
  }

  @Override
  public Object solvePart1(String input) {
    parseInput(input);
    isBeacon = new HashSet<>(beacons);
    setMinAndMaxX();

    return countNonBeaconPositions(2000000);
  }

  @Override
  public Object solvePart2(String input) {
    parseInput(input);

    for (int current = 0; current < sensors.size(); current++) {
      var outlineDistance = distances.get(current) + 1;
      for (var xDistance = 0; xDistance < outlineDistance; xDistance++) {
        var yDistance = outlineDistance - xDistance;
        var xOutlines = List.of(sensors.get(current).x + xDistance, sensors.get(current).x - xDistance);
        var yOutlines = List.of(sensors.get(current).y + yDistance, sensors.get(current).y - yDistance);
        for (var xOutline : xOutlines) {
          for (var yOutline : yOutlines) {
            if (isUndiscovered(xOutline, yOutline)) {
              return 4000000L * xOutline + yOutline;
            }
          }
        }
      }
    }
    return 0;
  }

  private boolean isUndiscovered(int x, int y) {
    if (x < 0 || x > 4000000 || y < 0 || y > 4000000) return false;
    boolean isUndiscovered = true;
    for (int current = 0; current < sensors.size(); current++) {
      var currentDistance = abs(x - sensors.get(current).x) + abs(y - sensors.get(current).y);
      if (currentDistance <= distances.get(current)) {
        isUndiscovered = false;
        break;
      }
    }
    return isUndiscovered;
  }

  private int countNonBeaconPositions(int y) {
    int count = 0;
    for (int x = minX; x < maxX; x++) {
      for (int current = 0; current < sensors.size(); current++) {
        var position = position(x, y);
        var distance = distance(position, sensors.get(current));
        if (distance <= distances.get(current) && !beacons.contains(position)) {
          ++count;
          break;
        }
      }
    }
    return count;
  }

  private void setMinAndMaxX() {
    minX = Integer.MAX_VALUE;
    maxX = Integer.MIN_VALUE;
    for (int i = 0; i < sensors.size(); i++) {
      minX = min(minX, sensors.get(i).x - distances.get(i));
      maxX = max(maxX, sensors.get(i).x + distances.get(i));
    }
  }

  private void parseInput(String input) {
    sensors = new ArrayList<>();
    beacons = new ArrayList<>();
    distances = new ArrayList<>();

    for (String line : getLines(input)) {
      var parts = line.split(SPACE);
      var sensor = position(parseInt(keepDigits(parts[2]).trim()), parseInt(keepDigits(parts[3]).trim()));
      var beacon = position(parseInt(keepDigits(parts[8]).trim()), parseInt(keepDigits(parts[9]).trim()));
      sensors.add(sensor);
      beacons.add(beacon);
      distances.add(distance(sensor, beacon));
    }
  }

  int distance(Position c1, Position c2) {
    return abs(c1.x - c2.x) + abs(c1.y - c2.y);
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class Position {

    final int x, y;

    static Position position(int x, int y) {
      return new Position(x, y);
    }
  }
}