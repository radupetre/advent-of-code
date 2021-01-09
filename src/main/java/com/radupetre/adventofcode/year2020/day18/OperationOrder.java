package com.radupetre.adventofcode.year2020.day18;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Long.parseLong;
import static java.lang.Long.valueOf;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/18
 */
@Service
@Log4j2
public class OperationOrder extends AbstractAdventSolution {

  private static final String SPACE = " ";
  private static final String SUB_FORMULA_START = "(";
  private static final String SUB_FORMULA_END = ")";
  private static final String PLUS = "+";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 18);
  }
  
  @Override
  public Object solvePart1(String input) {
    return getLines(input)
        .stream()
        .mapToLong(formula -> evaluateFormulaResult(formula, this::solveFormulaNoPrecedence))
        .sum();
  }

  private long evaluateFormulaResult(String formula, Function<String, Long> solver) {
    while (formula.contains(SUB_FORMULA_START)) {
      int subFormulaEnd = formula.indexOf(SUB_FORMULA_END);
      int subFormulaStart = formula.lastIndexOf(SUB_FORMULA_START, subFormulaEnd);

      // solve and replace expressions between parenthesis
      formula = formula.substring(0, subFormulaStart)
          + solver.apply(formula.substring(subFormulaStart + 1, subFormulaEnd))
          + formula.substring(subFormulaEnd + 1);
    }

    return solver.apply(formula);
  }

  private long solveFormulaNoPrecedence(String formula) {
    //solve in order, without considering precedence
    final LinkedList<String> formulaParts = new LinkedList<>(Arrays.asList(formula.split(SPACE)));

    // consume two terms and a binary operator
    // add back the resulting term
    while (formulaParts.size() > 1) {
      formulaParts.addFirst(calculate(
          formulaParts.removeFirst(),
          formulaParts.removeFirst(),
          formulaParts.removeFirst())
      );
    }

    return parseLong(formulaParts.removeFirst());
  }

  private String calculate(String value1, String operator, String value2) {
    Long term1 = valueOf(value1);
    Long term2 = valueOf(value2);

    Long resultingTerm = PLUS.equals(operator)
        ? term1 + term2
        : term1 * term2;

    return resultingTerm.toString();
  }

  @Override
  public Object solvePart2(String input) {
    return getLines(input)
        .stream()
        .mapToLong(formula -> evaluateFormulaResult(formula, this::solveFormulaWithPrecedence))
        .sum();
  }

  private long solveFormulaWithPrecedence(String formula) {
    // solve with addition precedence, then multiplication
    final LinkedList<String> formulaParts = new LinkedList<>(Arrays.asList(formula.split(SPACE)));
    final LinkedList<String> multiplyParts = new LinkedList<>();

    // first consume additions
    while (formulaParts.size() > 1) {
      if (formulaParts.get(1).equals(PLUS)) {
        // solve additions on the spot
        formulaParts.addFirst(calculate(
            formulaParts.removeFirst(),
            formulaParts.removeFirst(),
            formulaParts.removeFirst())
        );
      } else {
        // deffer multiplications
        multiplyParts.add(formulaParts.removeFirst());
        multiplyParts.add(formulaParts.removeFirst());
      }
    }

    if (multiplyParts.isEmpty()) {
      // only had additions to solve
      return parseLong(formulaParts.removeFirst());
    }

    // now solve the multiplications
    multiplyParts.add(formulaParts.removeFirst());

    while (multiplyParts.size() > 1) {
      multiplyParts.addFirst(calculate(
          multiplyParts.removeFirst(),
          multiplyParts.removeFirst(),
          multiplyParts.removeFirst())
      );
    }

    return parseLong(multiplyParts.removeFirst());
  }

}
