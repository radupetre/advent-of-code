package com.radupetre.adventofcode.service;

import static java.util.Comparator.comparing;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionOrchestrator {

  private final Collection<AbstractAdventSolution> solutions;

  private final SolutionHandler solutionHandler;

  public void runSingleSolution(int year, int day) {
    AbstractAdventSolution adventSolution = solutions.stream()
        .filter(solution -> matchesContext(solution, new SolveContext(year, day)))
        .findFirst().orElseThrow(() -> new UnsupportedOperationException(
            String.format("No Solution solving advent for year:%s day:%s", year, day)));

    solutionHandler.handle(adventSolution);
  }

  private boolean matchesContext(AbstractAdventSolution adventSolution,
      SolveContext desiredContext) {
    return desiredContext.equals(adventSolution.getSolveContext());
  }

  public void runSolutions() {
    solutions.stream()
        .sorted(comparing(AbstractAdventSolution::getSolveContext))
        .forEach(solutionHandler::handle);
  }
}
