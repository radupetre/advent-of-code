package com.radupetre.adventofcode.year2022.day10;

import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/10
 */
@Service
@Log4j2
public class CathodeRayTube extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 10);
  }

  @Override
  public Object solvePart1(String input) {
    Map<Integer, Integer> registerByCycle = new HashMap<>();
    int register = 1;
    int cycle = 1;

    for (var instruction : getLines(input)) {
      registerByCycle.put(cycle++, register);
      if (!instruction.equals("noop")) {
        registerByCycle.put(cycle++, register);
        register += parseInt(instruction.split(SPACE)[1]);
      }
    }

    return IntStream.range(0, 6)
        .map(n -> n * 40)
        .map(n -> n + 20)
        .map(n -> n * registerByCycle.get(n))
        .sum();
  }

  @Override
  public Object solvePart2(String input) {
    StringBuilder output = new StringBuilder();
    int register = 1;
    int crt = 0;

    for (var instruction : getLines(input)) {
      output.append((register - 1 <= crt && crt <= register + 1) ? '#' : '.');
      crt = (crt + 1) % 40;
      if (!instruction.equals("noop")) {
        output.append((register - 1 <= crt && crt <= register + 1) ? '#' : '.');
        crt = (crt + 1) % 40;
        register += parseInt(instruction.split(SPACE)[1]);
      }
    }

    return Pattern.compile(".{1,40}")
        .matcher(output)
        .results()
        .map(MatchResult::group)
        .collect(Collectors.joining("\n"));
  }
}