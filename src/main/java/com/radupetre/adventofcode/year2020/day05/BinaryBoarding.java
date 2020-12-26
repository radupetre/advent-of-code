package com.radupetre.adventofcode.year2020.day05;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/5
 */
@Service
@Log4j2
public class BinaryBoarding extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 05);
  }

  @Override
  public Object solvePart1(String boardingPasses) {
    final List<Integer> seatIds = getLines(boardingPasses)
        .stream()
        .mapToInt(this::calculateSeatId)
        .boxed()
        .collect(toList());

    return getHighestSeatId(seatIds);
  }

  @Override
  public Object solvePart2(String boardingPasses) {
    final List<Integer> seatIds = getLines(boardingPasses)
        .stream()
        .mapToInt(this::calculateSeatId)
        .boxed()
        .collect(toList());

    return getMissingSeatId(seatIds);
  }

  private int getMissingSeatId(List<Integer> seatIds) {
    int actualSeatIdsSum = 0;
    int minSeatId = Integer.MAX_VALUE;
    int maxSeatId = Integer.MIN_VALUE;

    for (int seatId : seatIds) {
      actualSeatIdsSum += seatId;
      minSeatId = seatId < minSeatId ? seatId : minSeatId;
      maxSeatId = seatId > maxSeatId ? seatId : maxSeatId;
    }

    int expectedSeatIdsSum = sumSeatsUpToId(maxSeatId) - sumSeatsUpToId(minSeatId - 1);
    int missingSeatId = expectedSeatIdsSum - actualSeatIdsSum;
    return missingSeatId;
  }

  private int getHighestSeatId(List<Integer> seatIds) {
    return seatIds.stream()
        .mapToInt(Integer::intValue)
        .max()
        .getAsInt();
  }

  private int calculateSeatId(String encodedBoardingPass) {
    String binaryBoardingPass = decodeToBinary(encodedBoardingPass);
    return Integer.parseInt(binaryBoardingPass, 2);
  }

  private String decodeToBinary(String encodedBoardingPass) {
    return encodedBoardingPass
        // replace row positions
        .replace('R', '1')
        .replace('L', '0')
        // replace seat positions
        .replace('B', '1')
        .replace('F', '0');
  }

  private int sumSeatsUpToId(int upperLimitSeatId) {
    return upperLimitSeatId * (upperLimitSeatId + 1) / 2;
  }
}
