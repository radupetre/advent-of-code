package com.radupetre.adventofcode.year2021.day08;

import static com.radupetre.adventofcode.utils.StringUtility.NEW_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.PIPE;
import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/8
 */
@Service
@Log4j2
public class SevenSegmentSearch extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 8);
  }

  private final Map<Integer, Set<Character>> wiresByDigits = Map.of(
      0, Set.of('a', 'b', 'c',      'e', 'f', 'g'),
      1, Set.of(          'c',           'f'     ),
      2, Set.of('a',      'c', 'd', 'e',      'g'),
      3, Set.of('a',      'c', 'd',      'f', 'g'),
      4, Set.of(     'b', 'c', 'd',      'f'     ),
      5, Set.of('a', 'b',      'd',      'f', 'g'),
      6, Set.of('a', 'b',      'd', 'e', 'f', 'g'),
      7, Set.of('a',      'c',           'f'     ),
      8, Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g'),
      9, Set.of('a', 'b', 'c', 'd',      'f', 'g')
  );

  // 2, 3, 4, 7 are unique wire sizes corresponding to numbers 1, 7, 4, 8
  private final List<Integer> uniqueWireSizes = Arrays.asList(2, 3, 4, 7);

  private final Map<String, Integer> numbersByHash = getNumbersByHash();

  @Override
  public Object solvePart1(String input) {
    return getBatchesAsStream(input, NEW_LINE)
        .map(String::trim)
        .map(inputLine -> inputLine.split(PIPE))
        .map(inputLineFragments -> inputLineFragments[1])
        .map(String::trim)
        .map(outputValues -> outputValues.split(SPACE))
        .flatMap(Arrays::stream)
        .map(String::trim)
        .map(String::length)
        .filter(uniqueWireSizes::contains)
        .count();
  }

  @Override
  public Object solvePart2(String input) {
    return getBatchesAsStream(input, NEW_LINE)
        .map(String::trim)
        .map(inputLine -> inputLine.split(PIPE))
        .map(Arrays::asList)
        .map(this::toOutputValue)
        .reduce(0, Integer::sum);
  }

  private int toOutputValue(List<String> inputLine) {
    final List<Set<Character>> wireSets = getCharSets(inputLine.get(0));
    final List<Set<Character>> outputSets = getCharSets(inputLine.get(1));
    final Map<Set<Character>, String> hashesByWireSets = getWireSetsHashes(wireSets);

    return outputSets.stream()
        .map(hashesByWireSets::get)
        .map(numbersByHash::get)
        .reduce(0, (result, digit) -> result * 10 + digit);
  }

  private Map<String, Integer> getNumbersByHash() {
    // Transform each set to it's respective hash and keep the digit<->hash relationship map to use
    final Map<Set<Character>, String> hashesByWireSet = getWireSetsHashes(wiresByDigits.values());
    return wiresByDigits.entrySet()
        .stream()
        .collect(toMap(
            e -> hashesByWireSet.get(e.getValue()),
            Entry::getKey)
        );
  }

  private Map<Set<Character>, String> getWireSetsHashes(Collection<Set<Character>> wireSets) {
    return wireSets.stream()
        .collect(toMap(
            identity(),
            wireSet -> calculateHash(wireSet, wireSets))
        );
  }

  private String calculateHash(Set<Character> wireSet, Collection<Set<Character>> wireSets) {
    // any complete collection of wire sets can be reduced to the same unique hashes:
    // 0#62336, 1#22222, 2#51225, 3#52335, 4#42244, 5#51235, 6#61236, 7#32323, 8#72347, 9#62346

    // digit #1 of the hash will be the number of wires
    StringBuilder hash = new StringBuilder(String.valueOf(wireSet.size()));

    // this already ensures a unique hash for digits that correspond to unique wire counts in part.1
    // digits #2-#5 will the counts for common wires between current set and the ..
    // known unique sets from part.1
    uniqueWireSizes.stream()
        .map(uniqueWireSize -> getSetOfSize(wireSets, uniqueWireSize))
        .forEach(setOfUniqueWireSize -> {
          setOfUniqueWireSize.retainAll(wireSet);
          int intersectionCount = setOfUniqueWireSize.size();
          hash.append(intersectionCount);
        });

    return hash.toString();
  }

  private Set<Character> getSetOfSize(Collection<Set<Character>> sets, int size) {
    return new HashSet<>(sets.stream()
        .filter(set -> set.size() == size)
        .findFirst()
        .orElseThrow());
  }

  private Set<Character> getChars(String from) {
    return from.chars()
        .mapToObj(e -> (char) e)
        .collect(Collectors.toSet());
  }

  private List<Set<Character>> getCharSets(String sentence) {
    return getBatchesAsStream(sentence.trim(), SPACE)
        .map(this::getChars)
        .collect(toList());
  }
}
