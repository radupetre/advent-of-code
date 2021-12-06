package com.radupetre.adventofcode.year2021.day06;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/6
 */
@Service
@Log4j2
public class Lanternfish extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 6);
  }

  @Override
  public Object solvePart1(String fishes) {
    return calculateFishNumbersAfterDays(fishes, 80);
  }

  @Override
  public Object solvePart2(String fishes) {
    return calculateFishNumbersAfterDays(fishes, 256);
  }

  Long calculateFishNumbersAfterDays(String fishes, int days) {
    Map<Integer, Long> currentFishCountByAge = getFishCountByAge(fishes);

    for (int day = 0; day < days; day++) {
      currentFishCountByAge = getNextDayFishCountByAge(currentFishCountByAge);
    }

    return currentFishCountByAge.values().stream().reduce(0L, Long::sum);
  }

  private Map<Integer, Long> getFishCountByAge(String fishes) {
    return getBatchesAsStream(fishes, COMMA)
        .map(String::trim)
        .map(Integer::parseInt)
        .collect(groupingBy(identity(), counting()));
  }

  private Map<Integer, Long> getNextDayFishCountByAge(Map<Integer, Long> currentFishCountByAge) {
    // age generations
    final Map<Integer, Long> nextFishCountByAge = currentFishCountByAge.entrySet().stream()
        .collect(toMap(
            entry -> entry.getKey() == 0 ? 8 : entry.getKey() - 1,
            Entry::getValue
        ));

    // reset old fish to 6
    Long existingCount = nextFishCountByAge.getOrDefault(6, 0L);
    Long fromReset = currentFishCountByAge.getOrDefault(0, 0L);
    nextFishCountByAge.put(6, existingCount + fromReset);

    return nextFishCountByAge;
  }
}
