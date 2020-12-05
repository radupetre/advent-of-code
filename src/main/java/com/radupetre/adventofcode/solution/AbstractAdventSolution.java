package com.radupetre.adventofcode.solution;

import org.springframework.stereotype.Service;

public abstract class AbstractAdventSolution {

  abstract public SolveContext getSolveContext();

  abstract public void solve(String input);
}
