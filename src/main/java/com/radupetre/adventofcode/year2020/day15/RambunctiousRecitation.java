package com.radupetre.adventofcode.year2020.day15;

import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.Result;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
  public Result solve(String inputNumbers) {
    final List<Integer> startingNumbers = Arrays.stream(inputNumbers.split(","))
        .map(Integer::valueOf)
        .collect(toList());

    int numberAfterFewSeps = getNthSpokenNumber(startingNumbers, 2020);
    log.info("2020th Spoken Number: %s".formatted(numberAfterFewSeps));

    int numberAfterManySteps = getNthSpokenNumber(startingNumbers, 30000000);
    log.info("30millionth Spoken Number: %s".formatted(numberAfterManySteps));

    return new Result(numberAfterFewSeps, numberAfterManySteps);
  }

  private int getNthSpokenNumber(List<Integer> startingNumbers, int targetPosition) {
    AtomicInteger currentPosition = new AtomicInteger(1);
    Map<Integer, Integer> lastOccurrenceOfNumber = new HashMap<>();

    // first add starting numbers
    startingNumbers.forEach(startingNumber -> {
      lastOccurrenceOfNumber.put(startingNumber, currentPosition.getAndIncrement());
    });

    boolean firstTimeSpoken = true;
    int turnsSpokenApart = 0;
    int nextNumber = 0;

    // then apply the rules
    while (currentPosition.get() <= targetPosition) {
      nextNumber = firstTimeSpoken ? 0 : turnsSpokenApart;
      turnsSpokenApart = currentPosition.get() - lastOccurrenceOfNumber.getOrDefault(nextNumber, 0);
      firstTimeSpoken = !lastOccurrenceOfNumber.containsKey(nextNumber);
      lastOccurrenceOfNumber.put(nextNumber, currentPosition.getAndIncrement());
    }

    return nextNumber;
  }
}
