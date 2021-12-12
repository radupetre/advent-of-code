package com.radupetre.adventofcode.year2021.day12;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/12
 */
@Service
@Log4j2
public class PassagePathing extends AbstractAdventSolution {

  private static final String START = "start";
  private static final String END = "end";

  Map<String, Cave> cavesByName = new HashMap<>();
  Set<Cave> smallCavesVisited = new HashSet<>();
  Cave smallCaveVisitedTwice = null;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 12);
  }

  @Override
  public Object solvePart1(String input) {
    cavesByName = readCaves(getLines(input));

    Cave startingCave = cavesByName.get(START);
    return countPathsToEnd(startingCave);
  }

  @Override
  public Object solvePart2(String input) {
    cavesByName = readCaves(getLines(input));

    Cave startingCave = cavesByName.get(START);
    return countPathsToEndWithDoubleVisit(startingCave);
  }

  private int countPathsToEnd(Cave currentCave) {
    // count the path since we've reached the end
    if (END.equals(currentCave.name)) {
      return 1;
    }

    // already visited this small cave, won't count
    if (smallCavesVisited.contains(currentCave)) {
      return 0;
    }

    if (currentCave.isSmall) {
      smallCavesVisited.add(currentCave);
    }

    // visit linked caves
    int pathsToEnd = currentCave.connections.stream()
        .map(this::countPathsToEnd)
        .reduce(0, Integer::sum);

    smallCavesVisited.remove(currentCave);
    return pathsToEnd;
  }

  private int countPathsToEndWithDoubleVisit(Cave currentCave) {
    // count the path since we've reached the end
    if (END.equals(currentCave.name)) {
      return 1;
    }

    if (smallCavesVisited.contains(currentCave)) {
      if (smallCaveVisitedTwice == null && !START.equals(currentCave.name)) {
        // this small cave can be visited twice
        smallCaveVisitedTwice = currentCave;
      } else {
        // either this small cave was already visited twice
        // or only visited once but another cave is already visited twice
        return 0;
      }
    }

    if (currentCave.isSmall) {
      smallCavesVisited.add(currentCave);
    }

    // visit linked caves
    int pathsToEnd = currentCave.connections.stream()
        .map(this::countPathsToEndWithDoubleVisit)
        .reduce(0, Integer::sum);

    if (smallCaveVisitedTwice == currentCave) {
      // we're done visiting this small cave twice
      smallCaveVisitedTwice = null;
    } else {
      // we're done visiting this small cave
      smallCavesVisited.remove(currentCave);
    }

    return pathsToEnd;
  }

  private Map<String, Cave> readCaves(List<String> caveLinks) {
    Map<String, Cave> cavesByName = new HashMap<>();

    for (String caveLink : caveLinks) {
      final String[] caves = caveLink.trim().split("-");

      Cave cave1 = cavesByName.computeIfAbsent(caves[0], Cave::new);
      Cave cave2 = cavesByName.computeIfAbsent(caves[1], Cave::new);

      cave1.connections.add(cave2);
      cave2.connections.add(cave1);
    }
    return cavesByName;
  }
}

class Cave {
  String name;
  boolean isSmall;
  public List<Cave> connections = new ArrayList<>();

  Cave(String name) {
    this.name = name;
    if (name.toLowerCase().equals(name)) {
      isSmall = true;
    }
  }
}