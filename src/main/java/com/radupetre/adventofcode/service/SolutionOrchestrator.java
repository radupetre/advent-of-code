package com.radupetre.adventofcode.service;

import static java.util.Comparator.comparing;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
@Log4j2
@RequiredArgsConstructor
public class SolutionOrchestrator {

  private final Collection<AbstractAdventSolution> solutions;

  private final SolutionHandler solutionHandler;

  public void runSingleSolution(int year, int day) {
    AbstractAdventSolution adventSolution = solutions.stream()
        .filter(solution -> matchesContext(solution, new SolveContext(year, day)))
        .findFirst().orElseThrow(() -> new UnsupportedOperationException(
            String.format("No Solution solving advent for year:%s day:%s", year, day)));

    StopWatch solveWatch = getWatch();
    solutionHandler.handle(adventSolution, solveWatch);
    log.info(solveWatch.prettyPrint());
  }

  private boolean matchesContext(AbstractAdventSolution adventSolution,
      SolveContext desiredContext) {
    return desiredContext.equals(adventSolution.getSolveContext());
  }

  public void runSolutions() {
    StopWatch solveWatch = getWatch();

    solutions.stream()
        .sorted(comparing(AbstractAdventSolution::getSolveContext))
        .forEach(solution -> solutionHandler.handle(solution, solveWatch));

    log.info(solveWatch.prettyPrint());
  }

  private StopWatch getWatch() {
    return new StopWatch("SolveWatch");
  }
}
