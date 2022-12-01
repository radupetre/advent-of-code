package com.radupetre.adventofcode.year2022.day01;

import static com.radupetre.adventofcode.utils.StringUtility.EMPTY_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.Collections.reverseOrder;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/1
 */
@Service
@Log4j2
public class CalorieCounting extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 1);
  }

  @Override
  public Object solvePart1(String input) {
    return
        getBatchesAsStream(input, EMPTY_LINE)
            .mapToInt(elfSnacks -> getLinesAsStream(elfSnacks).mapToInt(Integer::parseInt).sum())
            .max()
            .getAsInt();
  }

  @Override
  public Object solvePart2(String input) {
    return
        getBatchesAsStream(input, EMPTY_LINE)
            .map(elfSnacks -> getLinesAsStream(elfSnacks).mapToInt(Integer::parseInt).sum())
            .sorted(reverseOrder())
            .limit(3)
            .mapToInt(elfTotalSnacks -> elfTotalSnacks)
            .sum();
  }

}
