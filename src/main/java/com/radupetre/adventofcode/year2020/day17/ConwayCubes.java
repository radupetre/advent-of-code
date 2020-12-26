package com.radupetre.adventofcode.year2020.day17;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/17
 */
@Service
@Log4j2
public class ConwayCubes extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 17);
  }

  @Override
  public Object solvePart1(String input) {
    Cubes startingCubes = Cubes.parseCubes(input);
    return countActiveCubesAfterCycles(startingCubes, 6);
  }

  @Override
  public Object solvePart2(String input) {
    HyperCubes startingHyperCubes = HyperCubes.parseHyperCubes(input);
    return countActiveHyperCubesAfterCycles(startingHyperCubes, 6);
  }

  private long countActiveCubesAfterCycles(Cubes currentCubes, int cycles) {
    // execute the cycles then count the active;
    for (int cycle = 0; cycle < cycles; cycle++) {
      currentCubes = currentCubes.getNextCubesCycle();
    }

    return countActive(currentCubes);
  }

  private long countActive(Cubes cubes) {
    return Arrays.stream(cubes.active)
        .flatMap(Arrays::stream)
        .mapToLong(this::countActive)
        .sum();
  }

  private long countActive(boolean[] active) {
    return IntStream.range(0, active.length)
        .mapToObj(position -> active[position])
        .filter(isActive -> isActive)
        .count();
  }

  private long countActiveHyperCubesAfterCycles(HyperCubes currentHyperCubes, int cycles) {
    // execute the cycles then count the active;
    for (int cycle = 0; cycle < cycles; cycle++) {
      currentHyperCubes = currentHyperCubes.getNextCubesCycle();
    }

    return countActive(currentHyperCubes);
  }

  private long countActive(HyperCubes hyperCubes) {
    return Arrays.stream(hyperCubes.active)
        .flatMap(Arrays::stream)
        .flatMap(Arrays::stream)
        .mapToLong(this::countActive)
        .sum();
  }
}

