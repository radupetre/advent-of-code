package com.radupetre.adventofcode.year2022.day03;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/3
 */
@Service
@Log4j2
public class RucksackReorganization extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 3);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .map(this::splitHalves)
        .map(halves -> getCommonType(halves.getLeft(), halves.getRight()))
        .mapToInt(this::toPriorityScore)
        .sum();
  }

  @Override
  public Object solvePart2(String input) {
    var rucksacks = getLinesAsStream(input)
        .collect(toList());

    List<String[]> groupsOf3 = new ArrayList<>();
    var iterator = rucksacks.iterator();
    while (iterator.hasNext()) {
      groupsOf3.add(Stream.of(iterator.next(), iterator.next(), iterator.next())
          .toArray(String[]::new));
    }

    return groupsOf3.stream()
        .map(this::getCommonType)
        .mapToInt(this::toPriorityScore)
        .sum();
  }

  private char getCommonType(String... rucksacks) {
    Set<Character> commonSet = new HashSet<>();

    Stream.of(rucksacks).forEach(rucksack -> {
      var typesInRucksack = rucksack.chars().mapToObj(c -> (char) c).collect(toSet());
      if (commonSet.isEmpty()) {
        commonSet.addAll(typesInRucksack);
      } else {
        commonSet.retainAll(typesInRucksack);
      }
    });

    return commonSet.iterator().next();
  }

  private Pair<String, String> splitHalves(String line) {
    final int mid = line.length() / 2;
    var firstHalf = line.substring(0, mid);
    var secondHalf = line.substring(mid);
    return Pair.of(firstHalf, secondHalf);
  }

  private int toPriorityScore(char letterType) {
    if (letterType >= 'A' && letterType <= 'Z') {
      return letterType - 'A' + 27;
    } else {
      return letterType - 'a' + 1;
    }
  }

}
