package com.radupetre.adventofcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.radupetre.adventofcode.service.SolutionHandler;
import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
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
    final String actualOutput = solution.solve(input).toString();

    // then
    assertThat(actualOutput)
        .as("Output for day %s year %s".formatted(solveContext.getDay(), solveContext.getYear()))
        .isEqualTo(expectedOutput);
  }

  private String getOutput(SolveContext solveContext) {
    return solutionHandler.fetchOutput(solveContext);
  }

  private String getInput(SolveContext solveContext) {
    return solutionHandler.fetchInput(solveContext);
  }

}
