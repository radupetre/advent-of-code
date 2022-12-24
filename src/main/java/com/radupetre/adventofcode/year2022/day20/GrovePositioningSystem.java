package com.radupetre.adventofcode.year2022.day20;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.math.BigInteger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/20
 */
@Service
@Log4j2
public class GrovePositioningSystem extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 20);
  }

  private List<Number> numbers;
  Number firstNumber;
  Number lastNumber;
  Number zero;
  BigInteger modulo;

  @Override
  public Object solvePart1(String input) {
    parseNumbers(input, 1);
    linkNumbers();
    mixNumbers(1);

    return zero.jump(1000).val
        .add(zero.jump(2000).val)
        .add(zero.jump(3000).val);
  }

  @Override
  public Object solvePart2(String input) {
    parseNumbers(input, 811589153);
    linkNumbers();
    mixNumbers(10);

    return zero.jump(1000).val
        .add(zero.jump(2000).val)
        .add(zero.jump(3000).val);
  }


  private void mixNumbers(int times) {
    for (var count = 0; count < times; count++) {
      for (var number : numbers) {
        // remove number
        number.prev.next = number.next;
        number.next.prev = number.prev;

        var positions = number.val.remainder(modulo);
        if (positions.compareTo(BigInteger.ZERO) < 0) {
          positions = positions.add(modulo);
        }
        var target = number.next.jump(positions.longValue());

        // put number back in
        target.prev.next = number;
        number.prev = target.prev;
        target.prev = number;
        number.next = target;
      }
    }
  }

  private void linkNumbers() {
    firstNumber = null;
    lastNumber = null;
    zero = null;

    for (var number : numbers) {
      if (number.val.equals(BigInteger.ZERO)) {
        zero = number;
      }
      if (firstNumber == null) {
        firstNumber = number;
      } else {
        lastNumber.next = number;
        number.prev = lastNumber;
      }
      lastNumber = number;
    }
    lastNumber.next = firstNumber;
    firstNumber.prev = lastNumber;
  }

  private void parseNumbers(String input, int multiplier) {
    numbers = getLinesAsStream(input)
        .map(Integer::parseInt)
        .map(BigInteger::valueOf)
        .map(value -> value.multiply(BigInteger.valueOf(multiplier)))
        .map(Number::new)
        .collect(toList());
    modulo = BigInteger.valueOf(numbers.size() - 1);
  }

  private Number move(Number number, int steps) {
    for (int count = 0; count < steps; count++) {
      number = number.next;
    }
    return number;
  }

  @RequiredArgsConstructor
  class Number {

    final BigInteger val;
    Number next;
    Number prev;

    public Number jump(long positions) {
      Number jump = this;
      for (int count = 0; count < positions; count++) {
        jump = jump.next;
      }
      return jump;
    }
  }

}