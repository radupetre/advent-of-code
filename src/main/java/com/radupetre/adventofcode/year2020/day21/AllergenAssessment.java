package com.radupetre.adventofcode.year2020.day21;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/21
 */
@Service
@Log4j2
public class AllergenAssessment extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 21);
  }

  @Override
  public Object solvePart1(String allergenInformation) {
    final List<FoodInfo> foodInfos = parseFoodInfo(allergenInformation);
    Map<String, String> allergenToKnownIngredient = getAllergenToKnownIngredients(foodInfos);

    return countIngredientsWithoutAllergens(foodInfos, allergenToKnownIngredient);
  }

  private long countIngredientsWithoutAllergens(List<FoodInfo> foodInfos,
      Map<String, String> allergenToKnownIngredient) {
    Set<String> knownAllergenIngredients = new HashSet<>(allergenToKnownIngredient.values());

    return foodInfos.stream()
        .map(foodInfo -> foodInfo.ingredients)
        .flatMap(Collection::stream)
        .filter(ingredient -> !knownAllergenIngredients.contains(ingredient))
        .count();
  }

  private List<FoodInfo> parseFoodInfo(String allergenInformation) {
    return getLines(allergenInformation)
        .stream()
        .map(FoodInfo::parseFoodInfo)
        .collect(toList());
  }

  private Map<String, String> getAllergenToKnownIngredients(List<FoodInfo> foodInfos) {
    Map<String, Set<String>> allergenToPossibleIngredients = new HashMap<>();
    Map<String, String> allergenToKnownIngredient = new HashMap<>();

    for (FoodInfo foodInfo : foodInfos) {
      for (String allergen : foodInfo.allergens) {
        final Set<String> possibleIngredients = allergenToPossibleIngredients
            .computeIfAbsent(allergen, k -> new HashSet<>(foodInfo.ingredients));
        possibleIngredients.retainAll(foodInfo.ingredients);
      }
    }

    // as long as we have allergens to map
    while (allergenToKnownIngredient.size() < allergenToPossibleIngredients.size()) {
      for (String allergen : allergenToPossibleIngredients.keySet()) {
        Set<String> possibleIngredients = allergenToPossibleIngredients.get(allergen);
        if (possibleIngredients.size() == 1) {
          // only 1 known ingredient matches the allergen
          String knownIngredient = possibleIngredients.iterator().next();
          allergenToKnownIngredient.put(allergen, knownIngredient);

          // remove the known ingredient from other sets
          allergenToPossibleIngredients
              .values()
              .forEach(possibleIngredientsSet -> possibleIngredientsSet.remove(knownIngredient));

          break;
        }
      }
    }

    return allergenToKnownIngredient;
  }

  @Override
  public Object solvePart2(String allergenInformation) {
    final List<FoodInfo> foodInfos = parseFoodInfo(allergenInformation);
    Map<String, String> allergenToKnownIngredient = getAllergenToKnownIngredients(foodInfos);

    return concatenateIngredientsSortedByAllergen(allergenToKnownIngredient);
  }

  private String concatenateIngredientsSortedByAllergen(
      Map<String, String> allergenToKnownIngredient) {
    final List<String> sortedIngredients = allergenToKnownIngredient.entrySet()
        .stream()
        .sorted(comparing(Entry::getKey))
        .map(Entry::getValue)
        .collect(toList());

    return String.join(",", sortedIngredients);
  }
}

