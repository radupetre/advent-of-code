package com.radupetre.adventofcode.year2020.day18;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.Set;
import java.util.Stack;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/18
 */
@Service
@Log4j2
public class OperationOrder extends AbstractAdventSolution {

  private static final char OPEN_PARENTHESIS = '(';

  private static final char CLOSE_PARENTHESIS = ')';

  private static final char PLUS = '+';

  private static final char MULTIPLY = '*';

  private static final char SPACE = ' ';

  private static final Set<Character> OPERATORS = Set.of(PLUS, MULTIPLY);

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 18);
  }

  @Override
  public Object solvePart1(String input) {

    final long sum1 = StringUtility.getLines(input)
        .stream()
        .mapToLong(this::evaluateFormulaResult)
        .sum();
    log.info("Sum of all results: %s".formatted(sum1));

    return sum1;
  }

  @Override
  public Object solvePart2(String input) {

    return null;
  }

  private Long evaluateFormulaResult(String formula) {
    Stack<Long> values = new Stack<>();
    Stack<Character> operators = new Stack<>();

    for (char formulaPart : formula.toCharArray()) {
      if (isPadding(formulaPart)) {
        continue;

      } else if (isOperator(formulaPart)) {
        // this is an operator
        operators.push(formulaPart);

      } else if (isOpenParenthesis(formulaPart)) {
        // this is an open parenthesis
        operators.push(formulaPart);

      } else if (isCloseParenthesis(formulaPart)) {
        // this is an open parenthesis
        operators.pop();

        while (!operators.isEmpty() && isOperator(operators.peek())) {
          Long newValue = calculate(values.pop(), values.pop(), operators.pop());
          values.push(newValue);
        }

      } else {
        // this is a numeric value to be parsed
        Long value = Long.parseLong(String.valueOf(formulaPart));

        if (!operators.isEmpty() && isOperator(operators.peek())) {
          Long calculatedValue = calculate(value, values.pop(), operators.pop());
          values.push(calculatedValue);

        } else {
          values.push(value);

        }
      }
    }

    return values.pop();
  }

  private Long calculate(Long value1, Long value2, Character operator) {
    return switch (operator) {
      case PLUS -> value1 + value2;
      default -> value1 * value2;
    };
  }

  public boolean isPadding(Character formulaPart) {
    return SPACE == formulaPart;
  }

  public boolean isOperator(Character formulaPart) {
    return OPERATORS.contains(formulaPart);
  }

  public boolean isOpenParenthesis(Character formulaPart) {
    return OPEN_PARENTHESIS == formulaPart;
  }

  public boolean isCloseParenthesis(Character formulaPart) {
    return CLOSE_PARENTHESIS == formulaPart;
  }
}
