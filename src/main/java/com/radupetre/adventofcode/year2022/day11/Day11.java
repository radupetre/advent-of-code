package com.radupetre.adventofcode.year2022.day11;

import static com.radupetre.adventofcode.utils.StringUtility.EMPTY_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.cleanSpaces;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static com.radupetre.adventofcode.utils.StringUtility.keepDigits;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/11
 */
@Service
@Log4j2
public class Day11 extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 11);
  }

  private static final boolean WITH_REDUCE_WORRY = true;
  private static final boolean DONT_REDUCE_WORRY = false;

  @Override
  public Object solvePart1(String input) {
    var monkeys = readMonkeys(input);
    executeRounds(monkeys, 20, WITH_REDUCE_WORRY);
    return multiplyMaxInspected(monkeys, 2);
  }

  @Override
  public Object solvePart2(String input) {
    var monkeys = readMonkeys(input);
    executeRounds(monkeys, 10000, DONT_REDUCE_WORRY);
    return multiplyMaxInspected(monkeys, 2);
  }

  private void executeRounds(List<Monkey> monkeys, int rounds, boolean reduceWorry) {
    Integer leastCommonMultiple = monkeys.stream()
        .map(Monkey::getDivisor)
        .reduce(Math::multiplyExact)
        .orElseThrow(() -> new IllegalStateException("Failed to find lcm"));

    for (int round = 0; round < rounds; round++) {
      for (var monkey : monkeys) {
        while (!monkey.items.isEmpty()) {
          monkey.inspectedCount.incrementAndGet();
          var item = monkey.items.removeFirst();
          item = monkey.operation.apply(item);
          if (reduceWorry) {
            item = item / 3;
          } else {
            item = item % leastCommonMultiple;
          }
          var outcome = monkey.condition.test(item);
          var toMonkey = outcome ? monkey.outcomeTrueMonkey : monkey.outcomeFalseMonkey;
          monkeys.get(toMonkey).items.addLast(item);
        }
      }
    }
  }

  private Long multiplyMaxInspected(List<Monkey> monkeys, int count) {
    return monkeys.stream()
        .map(Monkey::getInspectedCount)
        .map(AtomicInteger::get)
        .sorted(reverseOrder())
        .limit(count)
        .map(Integer::longValue)
        .reduce(Math::multiplyExact)
        .orElseThrow(() -> new IllegalStateException("Can't find maximum inspected"));
  }

  private List<Monkey> readMonkeys(String input) {
    return getBatchesAsStream(input, EMPTY_LINE)
        .map(this::readMonkey)
        .collect(toList());
  }

  private Monkey readMonkey(String input) {
    var inputLines = getLinesAsStream(input)
        .map(String::trim)
        .collect(toList());

    var items = getBatchesAsStream(cleanSpaces(keepDigits(inputLines.get(1))), SPACE)
        .map(Long::parseLong)
        .collect(toCollection(ArrayDeque::new));

    var operationParts = inputLines.get(2).split(SPACE);
    BinaryOperator<Long> operator = switch (operationParts[4]) {
      case "*" -> Math::multiplyExact;
      case "+" -> Math::addExact;
      default -> throw new IllegalStateException("Invalid operator " + operationParts[4]);
    };
    UnaryOperator<Long> operation = operationParts[5].equals("old")
        ? (old) -> operator.apply(old, old)
        : (old) -> operator.apply(old, parseLong(operationParts[5]));

    var divisor = parseInt(inputLines.get(3).split(SPACE)[3]);
    Predicate<Long> condition = (number) -> number % divisor == 0;

    var outcomeTrueMonkey = parseInt(inputLines.get(4).split(SPACE)[5]);
    var outcomeFalseMonkey = parseInt(inputLines.get(5).split(SPACE)[5]);

    return new Monkey(items, operation, divisor, condition, outcomeTrueMonkey, outcomeFalseMonkey);
  }

  @Getter
  @RequiredArgsConstructor
  static class Monkey {
    final Deque<Long> items;
    final UnaryOperator<Long> operation;
    final int divisor;
    final Predicate<Long> condition;
    final Integer outcomeTrueMonkey;
    final Integer outcomeFalseMonkey;
    AtomicInteger inspectedCount = new AtomicInteger(0);
  }
}