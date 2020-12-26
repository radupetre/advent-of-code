package com.radupetre.adventofcode.year2020.day10;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Math.max;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/10
 */
@Service
@Log4j2
public class AdapterArray extends AbstractAdventSolution {

  private static final Integer OUTLET_JOLTS = 0;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 10);
  }

  @Override
  public Object solvePart1(String adapterValues) {
    final List<Integer> sortedAdapters = getSortedAdapters(adapterValues);
    return getJoltDifferencesMultiplied(sortedAdapters);
  }

  @Override
  public Object solvePart2(String adapterValues) {
    final List<Integer> sortedAdapters = getSortedAdapters(adapterValues);
    return countDistinctAdapterArrangements(sortedAdapters);
  }

  private List<Integer> getSortedAdapters(String adapterValues) {
    return getLines(adapterValues).stream()
        .map(Integer::valueOf)
        .sorted()
        .collect(toList());
  }

  private long countDistinctAdapterArrangements(List<Integer> sortedAdapters) {
    int adapterCount = sortedAdapters.size();
    Map<Integer, Long> adapterArrangements = new HashMap<>();

    // start from the end, base new arrangements on existing ones
    for (int adapterPosition = adapterCount - 1; adapterPosition >= 0; adapterPosition--) {
      int adapterValue = sortedAdapters.get(adapterPosition);
      Long arrangementsForPosition =
          getCurrentArrangementsFromPreviousOnes(adapterValue, adapterArrangements);

      adapterArrangements.put(adapterValue, arrangementsForPosition);
    }

    return getCurrentArrangementsFromPreviousOnes(OUTLET_JOLTS, adapterArrangements);
  }

  private Long getCurrentArrangementsFromPreviousOnes(int adapterValue,
      Map<Integer, Long> adapterArrangements) {
    Long arrangementsForAdapter =
        adapterArrangements.getOrDefault(adapterValue + 1, 0L)
            + adapterArrangements.getOrDefault(adapterValue + 2, 0L)
            + adapterArrangements.getOrDefault(adapterValue + 3, 0L);

    return max(arrangementsForAdapter, 1L);
  }

  private long getJoltDifferencesMultiplied(List<Integer> sortedAdapters) {
    final Map<Integer, Long> joltDifferenceByOccurrence = range(0, sortedAdapters.size() - 1)
        .mapToObj(position -> sortedAdapters.get(position + 1) - sortedAdapters.get(position))
        .collect(groupingBy(identity(), counting()));

    // add the last 3 jolt difference
    joltDifferenceByOccurrence.computeIfPresent(3, (k, v) -> ++v);

    // add the first difference to 0 outlet
    joltDifferenceByOccurrence.computeIfPresent(sortedAdapters.get(0), (k, v) -> ++v);

    return joltDifferenceByOccurrence.get(1) * joltDifferenceByOccurrence.get(3);
  }
}
