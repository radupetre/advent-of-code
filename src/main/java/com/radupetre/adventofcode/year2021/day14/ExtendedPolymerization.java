package com.radupetre.adventofcode.year2021.day14;

import static com.radupetre.adventofcode.utils.StringUtility.ARROW;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/14
 */
@Service
@Log4j2
public class ExtendedPolymerization extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 14);
  }

  @Override
  public Object solvePart1(String input) {
    return resultAfterNSteps(input, 10);
  }

  @Override
  public Object solvePart2(String input) {
    return resultAfterNSteps(input, 40);
  }

  private long resultAfterNSteps(String input, int steps) {
    List<String> polymerAndRules = StringUtility.getBatches(input, StringUtility.EMPTY_LINE);

    String polymer = polymerAndRules.get(0);
    Map<String, Long> countByPair = parsePolymer(polymer);
    Map<String, Character> insertionsByPair = parseRules(polymerAndRules.get(1));

    // update the pair counts after each step
    for (int step = 0; step < steps; step++) {
      countByPair = getCountsAfterInsertion(countByPair, insertionsByPair);
    }

    // calculate most common - least common
    List<Long> sortedCounts = getSortedCounts(countByPair, polymer);
    return sortedCounts.get(sortedCounts.size() - 1) - sortedCounts.get(0);
  }

  private List<Long> getSortedCounts(Map<String, Long> countByPair, String polymer) {
    final Map<Character, Long> countByCharacter = new HashMap<>();

    countByPair.forEach((pair, count) -> {
      countByCharacter.merge(pair.charAt(0), count, Long::sum);
      countByCharacter.merge(pair.charAt(1), count, Long::sum);
    });

    // increase the counts for the start/end characters since they're missing out on a pair
    countByCharacter.merge(polymer.charAt(0), 1L, Long::sum);
    countByCharacter.merge(polymer.charAt(polymer.length() - 1), 1L, Long::sum);

    // now every character is two pairs so halve everything and sort
    return countByCharacter.values()
        .stream().map(count -> count / 2)
        .sorted()
        .collect(toList());
  }

  private Map<String, Long> getCountsAfterInsertion(Map<String, Long> oldCountByPair,
      Map<String, Character> insertionsByPair) {
    Map<String, Long> newCountByPair = new HashMap<>();

    oldCountByPair.forEach((oldPair, count) -> {
      final Character insertion = insertionsByPair.get(oldPair);
      newCountByPair.merge("" + oldPair.charAt(0) + insertion, count, Long::sum);
      newCountByPair.merge("" + insertion + oldPair.charAt(1), count, Long::sum);
    });

    return newCountByPair;
  }

  private Map<String, Long> parsePolymer(String startingPolymer) {
    return IntStream.range(0, startingPolymer.length() - 1)
        .mapToObj(pairPosition -> startingPolymer.substring(pairPosition, pairPosition + 2))
        .collect(groupingBy(identity(), counting()));
  }

  private Map<String, Character> parseRules(String rulesList) {
    return getLinesAsStream(rulesList.trim())
        .map(mapping -> mapping.split(ARROW))
        .collect(toMap(
            mapping -> mapping[0].trim(),
            mapping -> mapping[1].trim().charAt(0)));
  }
}
