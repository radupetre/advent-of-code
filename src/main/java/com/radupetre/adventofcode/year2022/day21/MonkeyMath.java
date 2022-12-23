package com.radupetre.adventofcode.year2022.day21;

import static com.radupetre.adventofcode.utils.StringUtility.COLON;
import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.lang.Math.abs;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/21
 */
@Service
@Log4j2
public class MonkeyMath extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 21);
  }

  static Map<String, Monkey> monkeysByName;

  @Override
  public Object solvePart1(String input) {
    parseMonkeys(input);
    return monkeysByName.get("root").yellNumber();
  }

  @Override
  public Object solvePart2(String input) {
    parseMonkeys(input);

    Monkey controllable = monkeysByName.get("humn");
    Monkey left = monkeysByName.get(monkeysByName.get("root").leftName);
    Monkey right = monkeysByName.get(monkeysByName.get("root").rightName);

    controllable.value = 1L;
    var leftBeforeChange = left.yellNumber();
    controllable.value = 1000L;
    var leftAfterChange = left.yellNumber();

    long targetValue = leftBeforeChange == leftAfterChange
        ? left.yellNumber()
        : right.yellNumber();
    Monkey measurable = leftBeforeChange == leftAfterChange
        ? right
        : left;

    long min = 1L;
    long max = 10_000_000_000_000L;
    var match = binarySearch(min, max, controllable, measurable, targetValue);

    // there might be several values that yield equality, we move backwards to find the smallest
    controllable.value = match - 1;
    while (targetValue == measurable.yellNumber()) {
      match--;
      controllable.value = match - 1;
    }

    return match;
  }

  private long binarySearch(long min, long max, Monkey controllable, Monkey measurable, long targetValue) {
    if (min == max || abs(max - min) == 1) {
      return min;
    }

    long mid = (min + max) / 2;
    controllable.value = mid;
    var actualValue = abs(measurable.yellNumber());

    return actualValue < targetValue
        ? binarySearch(min, mid, controllable, measurable, targetValue)
        : binarySearch(mid, max, controllable, measurable, targetValue);
  }

  private void parseMonkeys(String input) {
    monkeysByName = new HashMap<>();
    getLinesAsStream(input)
        .forEach(this::parseMonkey);
  }

  private void parseMonkey(String input) {
    var parts = input.split(COLON);
    var name = parts[0].trim();
    Monkey monkey = new Monkey(name);
    monkeysByName.put(name, monkey);

    var operations = parts[1].trim().split(SPACE);
    if (operations.length == 1) {
      monkey.value = Long.parseLong(operations[0]);
    } else {
      monkey.leftName = operations[0].trim();
      monkey.rightName = operations[2].trim();
      monkey.operator = switch (operations[1].trim()) {
        case "+" -> Math::addExact;
        case "-" -> Math::subtractExact;
        case "*" -> Math::multiplyExact;
        case "/" -> (a, b) -> a / b;
        default -> throw new IllegalStateException();
      };
    }
  }

  @RequiredArgsConstructor
  static class Monkey {

    final String name;
    Long value;
    String leftName;
    String rightName;
    BinaryOperator<Long> operator;

    public long yellNumber() {
      return value != null
          ? value
          : operator.apply(monkeysByName.get(leftName).yellNumber(), monkeysByName.get(rightName).yellNumber());
    }
  }
}