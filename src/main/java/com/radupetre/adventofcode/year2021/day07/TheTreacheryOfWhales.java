package com.radupetre.adventofcode.year2021.day07;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static java.lang.Math.abs;
import static java.lang.Math.round;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/7
 */
@Service
@Log4j2
public class TheTreacheryOfWhales extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 7);
  }

  Function<Integer, Integer> DIRECT_FUEL_COST = (distance) -> distance;
  Function<Integer, Integer> TRIANGULAR_FUEL_COST = (distance) -> distance * (distance + 1) / 2;

  @Override
  public Object solvePart1(String input) {
    final List<Integer> crabPositions = parsePositions(input);

    int bestPosition = (int) calculateMedian(crabPositions);

    return calculateFuel(crabPositions, bestPosition, DIRECT_FUEL_COST);
  }

  @Override
  public Object solvePart2(String input) {
    final List<Integer> crabPositions = parsePositions(input);

    int bestPositionGuess = (int) calculateMean(crabPositions);

    // it's possible for the best position to be either side of the mean so we'll adjust for that
    return IntStream.range(-1, 2)
        .map(positionAdjustment -> positionAdjustment + bestPositionGuess)
        .map(adjustedPosition -> calculateFuel(crabPositions, bestPositionGuess - 1,
            TRIANGULAR_FUEL_COST))
        .min()
        .getAsInt();
  }

  private int calculateFuel(List<Integer> crabPositions, int targetPosition,
      Function<Integer, Integer> fuelCalculation) {
    return crabPositions.stream()
        .map(crabPosition -> abs(crabPosition - targetPosition))
        .map(fuelCalculation::apply)
        .reduce(0, Integer::sum);
  }

  public long calculateMedian(List<Integer> numbers) {
    numbers.sort(Integer::compare);
    int middle = numbers.size() / 2;
    if (middle % 2 == 0) {
      return round(((double) numbers.get(middle - 1) + numbers.get(middle)) / 2);
    } else {
      return numbers.get(middle);
    }
  }

  public long calculateMean(List<Integer> numbers) {
    final int sum = numbers.stream().reduce(0, Integer::sum);
    return round((double) sum / numbers.size());
  }

  private List<Integer> parsePositions(String positions) {
    return getBatchesAsStream(positions, COMMA)
        .map(String::trim)
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }
}
