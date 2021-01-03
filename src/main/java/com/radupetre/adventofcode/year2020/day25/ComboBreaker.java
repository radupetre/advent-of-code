package com.radupetre.adventofcode.year2020.day25;

import static java.lang.Long.parseLong;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/25
 */
@Service
@Log4j2
public class ComboBreaker extends AbstractAdventSolution {

  private static final long DEFAULT_SUBJECT_NUMBER = 7;
  private static final long DEFAULT_DIVISOR = 20201227;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 25);
  }

  @Override
  public Object solvePart1(String input) {
    final List<String> inputLines = StringUtility.getLines(input);

    long cardPublicKey = parseLong(inputLines.get(0));
    long doorPublicKey = parseLong(inputLines.get(1));

    // first find card loop number
    long subjectNumber = DEFAULT_SUBJECT_NUMBER;
    long cardLoopCounter = 1;
    while (subjectNumber != cardPublicKey) {
      subjectNumber = subjectNumber * DEFAULT_SUBJECT_NUMBER;
      subjectNumber = subjectNumber % DEFAULT_DIVISOR;
      cardLoopCounter++;
    }

    // then use door public key together with card loop number
    subjectNumber = doorPublicKey;
    for (int counter = 1; counter < cardLoopCounter; counter++) {
      subjectNumber = subjectNumber * doorPublicKey;
      subjectNumber = subjectNumber % DEFAULT_DIVISOR;
    }

    // encryption key will be the last calculated subject number
    return subjectNumber;
  }

  @Override
  public Object solvePart2(String input) {
    return null;
  }
}
