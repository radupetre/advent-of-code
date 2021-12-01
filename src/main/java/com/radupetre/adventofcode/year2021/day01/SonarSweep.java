package com.radupetre.adventofcode.year2021.day01;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/1
 */
@Service
@Log4j2
public class SonarSweep extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 1);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Integer> measurements = getEntriesFromInput(input);

    int increaseCount = 0;
    for (int pos = 0; pos < measurements.size() - 1; pos++) {
      if (measurements.get(pos) < measurements.get(pos + 1)) {
        increaseCount++;
      }
    }

    return increaseCount;
  }

  @Override
  public Object solvePart2(String input) {
    final List<Integer> measurements = getEntriesFromInput(input);

    int increaseCount = 0;
    for (int pos = 0; pos < measurements.size() - 3; pos++) {
      // comparing m(p) + m(p+1) + m(p+2) < m(p+1) + m(p+2) + m(p+3)
      // can be simplified to comparing just m(p) < m(p+3)
      if (measurements.get(pos) < measurements.get(pos + 3)) {
        increaseCount++;
      }
    }

    return increaseCount;
  }

  List<Integer> getEntriesFromInput(String input) {
    return getLines(input)
        .stream()
        .map(Integer::valueOf)
        .collect(Collectors.toList());
  }

}
