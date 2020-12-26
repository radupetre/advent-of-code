package com.radupetre.adventofcode.year2020.day07;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.Getter;

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
