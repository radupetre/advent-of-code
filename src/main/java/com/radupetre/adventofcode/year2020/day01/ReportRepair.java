package com.radupetre.adventofcode.year2020.day01;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/1
 */
@Service
@Log4j2
public class ReportRepair extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 1);
  }

  @Override
  public Object solvePart1(String input) {
    final Set<Integer> entries = getEntriesFromInput(input);
    Pair<Integer, Integer> numbers = findPairWithSum(entries, 2020);
    return numbers.getLeft() * numbers.getRight();
  }

  @Override
  public Object solvePart2(String input) {
    final Set<Integer> entries = getEntriesFromInput(input);
    Triple<Integer, Integer, Integer> numbers = findTripletWithSum(entries, 2020);
    return numbers.getLeft() * numbers.getMiddle() * numbers.getRight();
  }

  Set<Integer> getEntriesFromInput(String input) {
    return getLines(input)
        .stream()
        .map(Integer::valueOf)
        .collect(toSet());
  }

  private Pair<Integer, Integer> findPairWithSum(Set<Integer> entries, int expectedSum) {
    Set<Integer> desiredEntries = new HashSet<>();

    for (int entry : entries) {
      if (desiredEntries.contains(entry)) {
        // found an entry and it's desired pair
        return Pair.of(entry, expectedSum - entry);

      } else {
        // mark the desired pair we need to find
        desiredEntries.add(expectedSum - entry);

      }
    }

    return Pair.of(0, 0);
  }

  private Triple<Integer, Integer, Integer> findTripletWithSum(Set<Integer> entries, int expectedSum) {
    Set<Integer> desiredPairSums = new HashSet<>();
    Set<Integer> desiredEntries = new HashSet<>();
    Map<Integer, Pair<Integer, Integer>> pairsCache = new HashMap<>();

    for (int entry : entries) {

      if (desiredEntries.contains(entry)) {
        // found a triplet
        final Pair<Integer, Integer> pair = pairsCache.get(entry);
        return Triple.of(pair.getLeft(), pair.getRight(), entry);

      } else {
        // mark new possible pairs requiring a third entry
        for (int desiredPairSum : desiredPairSums) {
          if (entry < desiredPairSum) {
            int desiredEntry = desiredPairSum - entry;
            // this single entry would complete a triplet
            desiredEntries.add(desiredEntry);
            // keep the pair that cold form the triplet
            pairsCache.put(desiredEntry, Pair.of(entry, expectedSum - desiredPairSum));

          }
        }
      }

      // another pair to look for
      desiredPairSums.add(expectedSum - entry);
    }

    return Triple.of(0, 0, 0);
  }
}