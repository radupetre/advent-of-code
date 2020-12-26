package com.radupetre.adventofcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.radupetre.adventofcode.service.SolutionHandler;
import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdventOfCodeApplicationTests {

  @Autowired
  private List<AbstractAdventSolution> solutions;

  @Autowired
  private SolutionHandler solutionHandler;

  @Test
  void verifySolutionsOutput() {
    solutions.stream()
        .forEach(this::verifySolutionsOutput);
  }

  private void verifySolutionsOutput(AbstractAdventSolution solution) {
    // given
    final SolveContext solveContext = solution.getSolveContext();
    String input = getInput(solveContext);
    String expectedOutput = getOutput(solveContext);

    // when
    String part1 = String.valueOf(solution.solvePart1(input));
    String part2 = String.valueOf(solution.solvePart2(input));

    // then
    final List<String> expectedOutputLines = StringUtility.getLines(expectedOutput);
    assertThat(part1)
        .as("Output for day %s year %s part1"
            .formatted(solveContext.getDay(), solveContext.getYear()))
        .isEqualTo(expectedOutputLines.get(0));
    assertThat(part2)
        .as("Output for day %s year %s part2"
            .formatted(solveContext.getDay(), solveContext.getYear()))
        .isEqualTo(expectedOutputLines.get(1));
  }

  private String getOutput(SolveContext solveContext) {
    return solutionHandler.fetchOutput(solveContext);
  }

  private String getInput(SolveContext solveContext) {
    return solutionHandler.fetchInput(solveContext);
  }

}
