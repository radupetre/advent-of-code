package com.radupetre.adventofcode.year2020.day15;

import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/15
 */
@Service
@Log4j2
public class RambunctiousRecitation extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 15);
  }

  @Override
  public Object solvePart1(String inputNumbers) {
    final List<Integer> startingNumbers = Arrays.stream(inputNumbers.split(","))
        .map(Integer::valueOf)
        .collect(toList());

    return getNthSpokenNumber(startingNumbers, 2020);
  }

  @Override
  public Object solvePart2(String inputNumbers) {
    final List<Integer> startingNumbers = Arrays.stream(inputNumbers.split(","))
        .map(Integer::valueOf)
        .collect(toList());

    return getNthSpokenNumber(startingNumbers, 30_000_000);
  }

  private int getNthSpokenNumber(List<Integer> startingNumbers, int targetPosition) {
    int currentPosition = 1;
    Map<Integer, Integer> lastSeen = new HashMap<>();

    // first add starting numbers
    for (int startingNumber : startingNumbers) {
      lastSeen.put(startingNumber, currentPosition++);
    }

    boolean seen = false;
    int turnsSpokenApart = 0;
    int nextNumber = 0;

    // then apply the rules
    while (currentPosition <= targetPosition) {
      nextNumber = seen ? turnsSpokenApart : 0;
      turnsSpokenApart = currentPosition - lastSeen.getOrDefault(nextNumber, 0);
      seen = lastSeen.containsKey(nextNumber);
      lastSeen.put(nextNumber, currentPosition++);
    }

    return nextNumber;
  }
}
