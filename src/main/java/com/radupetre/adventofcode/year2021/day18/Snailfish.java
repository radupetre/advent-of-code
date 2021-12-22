package com.radupetre.adventofcode.year2021.day18;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static com.radupetre.adventofcode.year2021.day18.SnailNumber.asSnailNumber;
import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/18
 */
@Service
@Log4j2
public class Snailfish extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 18);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .map(SnailNumber::asSnailNumber)
        .reduce(Snailfish::reduce)
        .get()
        .getMagnitude();
  }

  @Override
  public Object solvePart2(String input) {
    final List<String> snailNumbers = getLinesAsStream(input)
        .collect(toList());

    final List<Pair<Integer, Integer>> pairs = range(0, snailNumbers.size())
        .mapToObj(a -> range(0, snailNumbers.size()).mapToObj(b -> Pair.of(a, b)))
        .flatMap(identity())
        .collect(toList());

    return pairs.stream()
        .map(pair -> {
          SnailNumber snailNumber1 = asSnailNumber(snailNumbers.get(pair.getLeft()));
          SnailNumber snailNumber2 = asSnailNumber(snailNumbers.get(pair.getRight()));
          long pairMagnitude = reduce(snailNumber1, snailNumber2).getMagnitude();
          return pairMagnitude;
        })
        .max(Long::compareTo)
        .get();
  }

  static SnailNumber reduce(SnailNumber snailNumber1, SnailNumber snailNumber2) {
    SnailNumber snailNumber = asSnailNumber(snailNumber1, snailNumber2);
    while (true) {
      if (explode(snailNumber)) {
        continue; // after a split start again
      } else if (!split(snailNumber)) {
        break; // nothing left to explode/split
      }
    }
    return snailNumber;
  }

  public static boolean split(SnailNumber snailNumber) {
    SnailNumber toSplit = snailNumber.getNumberWithValueGreaterThan(9);
    if (toSplit == null) {
      return false;
    } else {
      toSplit.left = asSnailNumber(toSplit.number / 2);
      toSplit.right = asSnailNumber(toSplit.number - (toSplit.number / 2));
      toSplit.number = null;
      return true;
    }
  }

  public static boolean explode(SnailNumber snailNumber) {
    SnailNumber toExplode = snailNumber.getNumberAtDepth(0, 4);
    if (toExplode == null) {
      return false;
    } else {
      List<SnailNumber> allNumbers = snailNumber.getNumbers(new LinkedList<>());
      incrementRelativePosition(-1, toExplode.left, allNumbers);
      incrementRelativePosition(+1, toExplode.right, allNumbers);
      toExplode.left = null;
      toExplode.right = null;
      toExplode.number = 0;
      return true;
    }
  }

  private static void incrementRelativePosition(int relativePosition, SnailNumber search,
      List<SnailNumber> allNumbers) {
    final int foundPosition = allNumbers.indexOf(search);
    final int targetPosition = foundPosition + relativePosition;
    if (0 <= foundPosition && 0 <= targetPosition && targetPosition < allNumbers.size()) {
      SnailNumber toModify = allNumbers.get(targetPosition);
      toModify.number = toModify.number + search.number;
    }
  }
}

@AllArgsConstructor
class SnailNumber {

  SnailNumber left;
  SnailNumber right;
  Integer number;

  static SnailNumber asSnailNumber(Integer number) {
    return new SnailNumber(null, null, number);
  }

  static SnailNumber asSnailNumber(SnailNumber left, SnailNumber right) {
    return new SnailNumber(left, right, null);
  }

  public static SnailNumber asSnailNumber(String snailNumber) {
    if (snailNumber.contains("[")) {
      int commaPosition = findCommaPosition(snailNumber);
      String leftSnailNumber = snailNumber.substring(1, commaPosition);
      String rightSnailNumber = snailNumber.substring(commaPosition + 1, snailNumber.length() - 1);
      return asSnailNumber(asSnailNumber(leftSnailNumber), asSnailNumber(rightSnailNumber));
    } else {
      return asSnailNumber(parseInt(snailNumber));
    }
  }

  private static int findCommaPosition(String snailNumber) {
    int bracketCount = -1;
    for (int position = 0; position < snailNumber.length(); position++) {
      switch (snailNumber.charAt(position)) {
        case '[' -> bracketCount++;
        case ']' -> bracketCount--;
        case ',' -> {
          if (bracketCount == 0) {
            return position;
          }
        }
      }
    }
    throw new IllegalStateException("Can't find comma position in " + snailNumber);
  }

  boolean isPair() {
    return isNull(number);
  }

  boolean isNumber() {
    return !isPair();
  }

  boolean hasNumbers() {
    return left != null && right != null && left.isNumber() && right.isNumber();
  }

  public List<SnailNumber> getNumbers(List<SnailNumber> numbers) {
    if (isNumber()) {
      numbers.add(this);
    } else {
      left.getNumbers(numbers);
      right.getNumbers(numbers);
    }
    return numbers;
  }

  public long getMagnitude() {
    return isNumber()
        ? number
        : 3 * left.getMagnitude() + 2 * right.getMagnitude();
  }

  // Depth first search for specific depth
  public SnailNumber getNumberAtDepth(int currentDepth, int wantedDepth) {
    return hasNumbers() && currentDepth == wantedDepth
        ? this
        : ofNullable(left)
            .map(leftPair -> leftPair.getNumberAtDepth(currentDepth + 1, wantedDepth))
            .orElseGet(() -> ofNullable(right)
                .map(rightPair -> rightPair.getNumberAtDepth(currentDepth + 1, wantedDepth))
                .orElse(null));
  }

  // Depth first search for a number
  public SnailNumber getNumberWithValueGreaterThan(int searchNumber) {
    return isNumber() && this.number > searchNumber
        ? this
        : ofNullable(this.left)
            .map(left -> left.getNumberWithValueGreaterThan(searchNumber))
            .orElseGet(() -> ofNullable(this.right)
                .map(right -> right.getNumberWithValueGreaterThan(searchNumber))
                .orElse(null));
  }
}
