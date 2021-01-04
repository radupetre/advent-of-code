package com.radupetre.adventofcode.year2020.day21;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class FoodInfo {

  final Set<String> ingredients;
  final Set<String> allergens;

  static FoodInfo parseFoodInfo(String allergenLine) {
    final String[] allergenLineParts =
        allergenLine
            .replace("(", "")
            .replace(")", "")
            .replace(",", "")
            .split("contains");
    Set<String> ingredients = stream(allergenLineParts[0].trim().split(" ")).collect(toSet());
    Set<String> allergens = stream(allergenLineParts[1].trim().split(" ")).collect(toSet());
    return new FoodInfo(ingredients, allergens);
  }
}
