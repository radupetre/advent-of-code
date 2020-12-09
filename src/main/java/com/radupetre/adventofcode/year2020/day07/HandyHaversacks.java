package com.radupetre.adventofcode.year2020.day07;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.Result;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Solving of https://adventofcode.com/2020/day/7
 */
@Service
@Log4j2
public class HandyHaversacks extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 7);
  }

  @Override
  public Result solve(String allRules) {
    final List<BagRule> bagRules = getLines(allRules).stream()
        .filter(StringUtils::hasLength)
        .map(BagRule::new)
        .collect(toList());

    long bagsContainingTarget = countBagsContainingTarget(bagRules, "shiny gold");
    log.info("Bags containing shiny gold: %s".formatted(bagsContainingTarget));

    long bagsContainedInTarget = countBagsContainedInTarget(bagRules, "shiny gold") - 1;
    log.info("Bags contained in shiny gold: %s".formatted(bagsContainedInTarget));

    return new Result(bagsContainingTarget, bagsContainedInTarget);
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

@Getter
class BagRule {

  private static final String NO_INNER_BAGS = "no other bags.";

  private final String name;
  private final List<InnerBag> innerBags;

  BagRule(String ruleString) {
    final String[] ruleFragments = ruleString.split("contain");
    this.name = ruleFragments[0]
        .replaceAll("bag.", "")
        .trim();

    if (NO_INNER_BAGS.equals(ruleFragments[1].trim())) {
      this.innerBags = List.of();
    } else {
      final String[] innerBagsFragments = ruleFragments[1].split(",");
      this.innerBags = stream(innerBagsFragments)
          .map(String::trim)
          .map(InnerBag::new)
          .collect(toList());
    }
  }
}

@Getter
class InnerBag {

  private final String name;
  private final int quantity;

  InnerBag(String innerBagString) {
    this.quantity = parseInt(innerBagString.split(" ")[0]);
    this.name = innerBagString
        .replaceAll("\\d", "")
        .replaceAll("bag.*", "")
        .trim();
  }
}