package com.radupetre.adventofcode.year2022.day06;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/6
 */
@Service
@Log4j2
public class TuningTrouble extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 6);
  }

  @Override
  public Object solvePart1(String input) {
    return findNonRepeatingSubstringOfLength(input, 4);
  }


  @Override
  public Object solvePart2(String input) {
    return findNonRepeatingSubstringOfLength(input, 14);
  }

  private int findNonRepeatingSubstringOfLength(String input, int length) {
    for (int position = 0; position < input.length(); position++) {
      if (position > length - 1) {
        var uniqueLetters = IntStream
            .range(position - length + 1, position + 1)
            .mapToObj(input::charAt)
            .collect(Collectors.toSet());
        if (uniqueLetters.size() == length) {
          return position + 1;
        }
      }
    }
    return 0;
  }

}
