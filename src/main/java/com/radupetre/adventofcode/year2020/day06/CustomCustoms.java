package com.radupetre.adventofcode.year2020.day06;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.Result;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/6
 */
@Service
@Log4j2
public class CustomCustoms extends AbstractAdventSolution {

  private static final String BATCH_SEPARATOR = "\n\n";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 6);
  }

  @Override
  public Result solve(String declarations) {
    final List<String> declarationBatches = getBatches(declarations, BATCH_SEPARATOR);

    long distinctAnswersInDeclarationBatch = countDistinctAnswersInDeclarationBatches(
        declarationBatches);
    log.info("Sum of distinct answers: %s".formatted(distinctAnswersInDeclarationBatch));

    long commonAnswersInDeclarationBatches = countCommonAnswersInDeclarationBatches(
        declarationBatches);
    log.info("Sum of common answers: %s".formatted(commonAnswersInDeclarationBatches));

    return new Result(distinctAnswersInDeclarationBatch, commonAnswersInDeclarationBatches);
  }

  private long countCommonAnswersInDeclarationBatches(List<String> declarationBatches) {
    return declarationBatches.stream()
        .mapToLong(this::countCommonAnswersInDeclarationBatch)
        .sum();
  }

  private long countCommonAnswersInDeclarationBatch(String declarationBatch) {
    return getLines(declarationBatch)
        .stream()
        .map(this::getDistinctAnswersInDeclaration)
        .reduce((firstSet, secondSet) -> {
          firstSet.retainAll(secondSet);
          return firstSet;
        })
        .map(Set::size)
        .orElse(0);
  }

  private Set<Character> getDistinctAnswersInDeclaration(String declaration) {
    return declaration.chars()
        .mapToObj(c -> (char) c)
        .collect(toSet());
  }

  private long countDistinctAnswersInDeclarationBatches(List<String> declarationBatches) {
    return declarationBatches.stream()
        .mapToLong(this::countDistinctAnswersInDeclarationBatch)
        .sum();
  }

  private long countDistinctAnswersInDeclarationBatch(String declarationBatch) {
    return declarationBatch.chars()
        .filter(Character::isLetter)
        .distinct()
        .count();
  }
}
