package com.radupetre.adventofcode.year2021.day10;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/10
 */
@Service
@Log4j2
public class SyntaxScoring extends AbstractAdventSolution {

  private final Set<Character> openBrackets = Set.of('(', '[', '{', '<');
  private final Map<Character, Character> matchingBrackets = Map.of(
      '(', ')',
      '[', ']',
      '{', '}',
      '<', '>');

  private final Map<Character, Integer> syntaxValues = Map.of(
      ')', 3,
      ']', 57,
      '}', 1197,
      '>', 25137
  );

  private final Map<Character, Integer> autocompleteValues = Map.of(
      ')', 1,
      ']', 2,
      '}', 3,
      '>', 4
  );

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 10);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .map(String::trim)
        .map(this::getCorruptedBracket)
        .filter(Objects::nonNull)
        .map(syntaxValues::get)
        .reduce(0, Integer::sum);
  }

  @Override
  public Object solvePart2(String input) {
    final List<Long> results = getLinesAsStream(input)
        .map(String::trim)
        .map(this::getMissingBrackets)
        .filter(Objects::nonNull)
        .map(this::getAutocompleteScore)
        .sorted()
        .collect(toList());
    long response = results.get(results.size() / 2);
    return response;
  }

  Character getCorruptedBracket(String bracketLine) {
    Stack<Character> brackets = new Stack<>();

    for (char bracket : bracketLine.toCharArray()) {
      if (openBrackets.contains(bracket)) {
        brackets.push(bracket);
      } else {
        Character expectedClose = matchingBrackets.get(brackets.pop());
        if (!expectedClose.equals(bracket)) {
          return bracket;
        }
      }
    }
    // this bracketLine isn't corrupted
    return null;
  }

  List<Character> getMissingBrackets(String bracketLine) {
    Stack<Character> brackets = new Stack<>();

    for (char bracket : bracketLine.toCharArray()) {
      if (openBrackets.contains(bracket)) {
        brackets.push(bracket);
      } else {
        Character expectedClose = matchingBrackets.get(brackets.pop());
        if (!expectedClose.equals(bracket)) {
          // this bracket line is corrupted, stop parsing for missing brackets.
          return null;
        }
      }
    }

    List<Character> missingBrackets = new ArrayList<>();
    while (!brackets.isEmpty()) {
      missingBrackets.add(matchingBrackets.get(brackets.pop()));
    }
    return missingBrackets;
  }

  long getAutocompleteScore(List<Character> missingBrackets) {
    long score = 0;
    for (Character missingBracket : missingBrackets) {
      score *= 5;
      score += autocompleteValues.get(missingBracket);
    }
    return score;
  }
}
