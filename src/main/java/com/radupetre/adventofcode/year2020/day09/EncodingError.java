package com.radupetre.adventofcode.year2020.day09;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/9
 */
@Service
@Log4j2
public class EncodingError extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 9);
  }

  @Override
  public Object solvePart1(String allNumbers) {
    final List<Long> numbers = getLines(allNumbers).stream()
        .map(Long::valueOf)
        .collect(toList());

    return findFirstNumberWithMissingSum(numbers, 25);

  }

  @Override
  public Object solvePart2(String allNumbers) {
    final List<Long> numbers = getLines(allNumbers).stream()
        .map(Long::valueOf)
        .collect(toList());

    long numberWithMissingSum = findFirstNumberWithMissingSum(numbers, 25);

    return minPlusMaxInIntervalWithSum(numbers, numberWithMissingSum);
  }

  private long minPlusMaxInIntervalWithSum(List<Long> numbers, long requiredSum) {
    Interval requiredInterval = findIntervalWithSum(numbers, requiredSum);
    return sumOfMinMaxInInterval(requiredInterval.firstPosition, requiredInterval.lastPosition,
        numbers);
  }

  private Interval findIntervalWithSum(List<Long> numbers, long requiredSum) {
    int firstPosition = 0;
    int lastPosition = 0;
    long sumBetweenPosition = numbers.get(firstPosition);

    while (firstPosition < numbers.size()) {
      if (sumBetweenPosition == requiredSum) {
        // return first & last positions with target sum
        return new Interval(firstPosition, lastPosition);

      } else if (sumBetweenPosition > requiredSum) {
        // sum is too big, remove a number from the start
        sumBetweenPosition -= numbers.get(firstPosition++);

      } else { // sumBetweenPosition < requiredSum
        // sum can be bigger, add another number at the end
        sumBetweenPosition += numbers.get(++lastPosition);
      }
    }

    throw new IllegalStateException("Interval not found");
  }

  private long sumOfMinMaxInInterval(int firstPosition, int lastPosition, List<Long> numbers) {
    long min = Long.MAX_VALUE;
    long max = Long.MIN_VALUE;

    for (int position = firstPosition; position <= lastPosition; position++) {
      long number = numbers.get(position);

      if (number < min) {
        min = number;
      }
      if (number > max) {
        max = number;
      }
    }

    return min + max;
  }

  private long findFirstNumberWithMissingSum(List<Long> numbers, int preambleSize) {
    Set<Long> preamble = range(0, preambleSize - 1)
        .mapToObj(numbers::get)
        .collect(Collectors.toSet());

    for (int current = preambleSize; current < numbers.size(); current++) {
      long number = numbers.get(current);
      if (preambleHasPairSum(preamble, number)) {
        preamble.add(number);
        preamble.remove(numbers.get(current - preambleSize));
      } else {
        return number;
      }
    }
    return 0;
  }

  private boolean preambleHasPairSum(Set<Long> preamble, long targetSum) {
    Set<Long> matchingPair = new HashSet<>();

    for (Long current : preamble) {
      if (matchingPair.contains(current)) {
        return true;
      } else {
        matchingPair.add(targetSum - current);
      }
    }
    return false;
  }
}

@RequiredArgsConstructor
class Interval {

  final int firstPosition;
  final int lastPosition;
}