package com.radupetre.adventofcode.year2020.day07;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Solving of https://adventofcode.com/2020/day/7
 */
@Service
@Log4j2
public class HandyHaversacks extends AbstractAdventSolution {

  public static final String SHINY_GOLD_BAG = "shiny gold";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 7);
  }

  @Override
  public Object solvePart1(String allRules) {
    final List<BagRule> bagRules = parseBagRules(allRules);
    return countBagsContainingTarget(bagRules, SHINY_GOLD_BAG);
  }

  @Override
  public Object solvePart2(String allRules) {
    final List<BagRule> bagRules = parseBagRules(allRules);
    return countBagsContainedInTarget(bagRules, SHINY_GOLD_BAG) - 1;
  }

  private List<BagRule> parseBagRules(String allRules) {
    return getLines(allRules).stream()
        .filter(StringUtils::hasLength)
        .map(BagRule::new)
        .collect(toList());
  }

  private long countBagsContainedInTarget(List<BagRule> bagRules, String targetBag) {
    Map<String, List<InnerBag>> outerBagToInnerBags = buildOuterBagToInnerBags(bagRules);

    return countBagsContainedRecursively(outerBagToInnerBags, targetBag);
  }

  private long countBagsContainedRecursively(Map<String, List<InnerBag>> outerBagToInnerBags,
      String targetBag) {
    final List<InnerBag> innerBags = outerBagToInnerBags.get(targetBag);

    if (innerBags.isEmpty()) {
      return 1;
    } else {
      long nestedCount = innerBags.stream()
          .mapToLong(innerBag -> innerBag.getQuantity() * countBagsContainedRecursively(
              outerBagToInnerBags, innerBag.getName()))
          .sum();

      return 1 + nestedCount;
    }
  }

  private Map<String, List<InnerBag>> buildOuterBagToInnerBags(List<BagRule> bagRules) {
    return bagRules.stream()
        .collect(toMap(BagRule::getName, BagRule::getInnerBags));
  }

  private long countBagsContainingTarget(List<BagRule> bagRules, String targetBag) {
    Map<String, Set<String>> innerBagToOuterBags = buildInnerBagToOuterBagMap(bagRules);

    Set<String> bagsAlreadyChecked = new HashSet<>();
    Set<String> bagsBeingChecked = innerBagToOuterBags.get(targetBag);
    int bagsFound = 0;

    while (!bagsBeingChecked.isEmpty()) {
      bagsFound += bagsBeingChecked.size();
      bagsAlreadyChecked.addAll(bagsBeingChecked);
      bagsBeingChecked = bagsBeingChecked.stream()
          .map(bag -> innerBagToOuterBags.getOrDefault(bag, emptySet()))
          .flatMap(Collection::stream)
          .collect(toSet());
      bagsBeingChecked.removeAll(bagsAlreadyChecked);
    }

    return bagsFound;
  }

  private Map<String, Set<String>> buildInnerBagToOuterBagMap(List<BagRule> bagRules) {
    Map<String, Set<String>> innerBagToOuterBags = new HashMap<>();

    bagRules.forEach(outerBag -> {
      outerBag.getInnerBags().forEach(innerBag -> {
        innerBagToOuterBags.computeIfAbsent(innerBag.getName(), k -> new HashSet<>()).add(
            outerBag.getName());
      });
    });

    return innerBagToOuterBags;
  }
}

