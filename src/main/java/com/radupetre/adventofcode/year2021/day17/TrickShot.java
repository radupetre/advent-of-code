package com.radupetre.adventofcode.year2021.day17;

import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.cleanSpaces;
import static com.radupetre.adventofcode.utils.StringUtility.keepDigits;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/17
 */
@Service
@Log4j2
public class TrickShot extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 17);
  }

  @Override
  public Object solvePart1(String input) {
    /*
      the maximum height (maxH) only depends on y; x doesn't affect the result.
      maxH is achieved with the biggest starting vertical velocity (maxV).
      the position maxH = maxV + (max V - 1 ) + .. + 2 + 1 = maxV ( maxV + 1) / 2

      So what's the maxV we can start at ?
      Any starting with maxV at H=0 will come downward and pass with -maxV through H=0.
      So we can pick our maxV as long as it won't overshoot the target as |yMin|-1;
     */
    final String[] coordinates = cleanSpaces(keepDigits(input)).split(SPACE);
    int targetMinY = parseInt(coordinates[2]);
    int maxV = abs(targetMinY) - 1;
    int maxH = maxV * (maxV + 1) / 2;
    return maxH;
  }

  @Override
  public Object solvePart2(String input) {
    final String[] coordinates = cleanSpaces(keepDigits(input)).split(SPACE);
    int targetMinX = parseInt(coordinates[0]);
    int targetMaxX = parseInt(coordinates[1]);
    int targetMinY = parseInt(coordinates[2]);
    int targetMaxY = parseInt(coordinates[3]);

    // optimise the search space
    int minVelocityX = (int) round(sqrt(targetMinX)); // anything less and we don't reach the target on X
    int maxVelocityX = targetMaxX; // anything more and we overshoot the target on X
    int minVelocityY = targetMinY; // anything less and we undershoot the target on Y
    int maxVelocityY = -targetMinY; // anything more and we jump over the target falling back on Y

    int targetHitCount = 0;
    for (int xVelocity = minVelocityX; xVelocity <= maxVelocityX; xVelocity++) {
      for (int yVelocity = minVelocityY; yVelocity <= maxVelocityY; yVelocity++) {

        int currentX = 0;
        int xPenalty = 0;
        int currentY = 0;
        int yPenalty = 0;

        while (true) {
          if (xPenalty >= xVelocity && currentX < targetMinX) {
            break; // short of the target
          }
          if (currentY < targetMinY) {
            break; // under the target
          }

          if (isBetween(currentX, targetMinX, targetMaxX) &&
              isBetween(currentY, targetMinY, targetMaxY)) {
            targetHitCount++; // within the target
            break;
          }

          currentX += max(xVelocity - xPenalty++, 0);
          currentY += yVelocity - yPenalty++;
        }
      }
    }

    return targetHitCount;
  }

  boolean isBetween(int value, int min, int max) {
    return (min <= value) && (value <= max);
  }

}
