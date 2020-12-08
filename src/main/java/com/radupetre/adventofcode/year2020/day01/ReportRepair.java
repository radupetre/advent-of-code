package com.radupetre.adventofcode.year2020.day01;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
  public void solve(String input) {
    final Set<Integer> entries = getLines(input)
        .stream()
        .map(Integer::valueOf)
        .collect(toSet());

    log.info(String.format("Product of pair with sum 2020: %s",
        productOfPairWithSum(entries, 2020)));
    log.info(String.format("Product of triplet with sum 2020: %s",
        productOfTripletWithSum(entries, 2020)));
  }

  private int productOfPairWithSum(Set<Integer> entries, int expectedSum) {
    Set<Integer> desiredEntries = new HashSet<>();

    for (int entry : entries) {
      if (desiredEntries.contains(entry)) {
        // found an entry and it's desired pair
        return entry * (expectedSum - entry);
      } else {
        // mark the desired pair we need to find
        desiredEntries.add(expectedSum - entry);
      }
    }

    return 0;
  }

  private int productOfTripletWithSum(Set<Integer> entries, int expectedSum) {
    Set<Integer> desiredPairSums = new HashSet<>();
    Set<Integer> desiredEntries = new HashSet<>();
    Map<Integer, Pair> pairsCache = new HashMap<>();

    for (int entry : entries) {

      if (desiredEntries.contains(entry)) {
        // found a triplet
        final Pair pair = pairsCache.get(entry);
        return pair.first * pair.second * entry;
      } else {
        // mark new possible pairs requiring a third entry
        for (int desiredPairSum : desiredPairSums) {
          if (entry < desiredPairSum) {
            int desiredEntry = desiredPairSum - entry;
            desiredEntries.add(desiredEntry);
            pairsCache.put(desiredEntry, new Pair(entry, expectedSum - desiredPairSum));
          }
        }
      }

      desiredPairSums.add(expectedSum - entry);
    }

    return 0;
  }
}

@RequiredArgsConstructor
class Pair {
  final int first;
  final int second;
}
