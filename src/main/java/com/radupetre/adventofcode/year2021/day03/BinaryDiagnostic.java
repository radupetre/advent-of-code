package com.radupetre.adventofcode.year2021.day03;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2021.day03.BitType.LEAST_COMMON_BIT;
import static com.radupetre.adventofcode.year2021.day03.BitType.MOST_COMMON_BIT;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/3
 */
@Service
@Log4j2
public class BinaryDiagnostic extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 3);
  }

  @Override
  public Object solvePart1(String input) {
    final List<String> numbers = getLines(input);
    final int bitCount = numbers.get(0).length();

    Map<Integer, Integer> countOnesByBits = countOnesByBits(numbers, bitCount);

    String mostCommonBits = "";
    String leastCommonBits = "";

    for (int currentBit = 0; currentBit < bitCount; currentBit++) {
      boolean isOneMostCommonBit =
          (countOnesByBits.get(currentBit) * 2) >= numbers.size();

      mostCommonBits += isOneMostCommonBit ? '1' : '0';
      leastCommonBits += isOneMostCommonBit ? '0' : '1';
    }

    return toInt(mostCommonBits) * toInt(leastCommonBits);
  }

  @Override
  public Object solvePart2(String input) {
    final List<String> numbers = getLines(input);

    String mostCommonBits = reduceByCommonBits(numbers, MOST_COMMON_BIT);
    String leastCommonBits = reduceByCommonBits(numbers, LEAST_COMMON_BIT);

    return toInt(mostCommonBits) * toInt(leastCommonBits);
  }

  private Map<Integer, Integer> countOnesByBits(List<String> numbers, int bitCount) {
    Map<Integer, Integer> countOnesByBits = new HashMap<>();

    range(0, bitCount)
        .forEach(
            currentBit -> countOnesByBits.put(currentBit, countOnesForBit(numbers, currentBit)));

    return countOnesByBits;
  }

  private int countOnesForBit(List<String> numbers, int currentBit) {
    return (int) numbers.stream()
        .map(number -> number.charAt(currentBit))
        .filter(bit -> bit == '1')
        .count();
  }


  private String reduceByCommonBits(List<String> startingNumbers, BitType bitType) {
    List<String> numbers = new ArrayList<>(startingNumbers);

    final MutableInt currentBit = new MutableInt(0);
    while (numbers.size() > 1) {
      int countOnesForCurrentBit = countOnesForBit(numbers, currentBit.getValue());
      boolean isOneMostCommonBit = (countOnesForCurrentBit * 2) >= numbers.size();

      numbers.removeIf(number -> {
        boolean isCurrentBitOne = number.charAt(currentBit.getValue()) == '1';
        boolean isCurrentBitMostCommon = isCurrentBitOne == isOneMostCommonBit;
        return MOST_COMMON_BIT.equals(bitType) != isCurrentBitMostCommon;
      });

      currentBit.increment();
    }

    return numbers.get(0);
  }

  private int toInt(String binaryNumber) {
    return Integer.parseInt(binaryNumber, 2);
  }

}

enum BitType {
  MOST_COMMON_BIT, LEAST_COMMON_BIT
}