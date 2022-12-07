package com.radupetre.adventofcode.year2022.day07;

import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/7
 */
@Service
@Log4j2
public class NoSpaceLeftOnDevice extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 7);
  }

  @Override
  public Object solvePart1(String input) {
    var sizeByPath = parseSizeByPath(input);

    return sizeByPath.values()
        .stream()
        .filter(size -> size < 100_000)
        .mapToInt(i -> i)
        .sum();
  }

  @Override
  public Object solvePart2(String input) {
    var sizeByPath = parseSizeByPath(input);

    int diskTotal = 70_000_000;
    int diskUsed = sizeByPath.get("[/]");

    int diskAvailable = diskTotal - diskUsed;
    int diskRequired = 30_000_000 - diskAvailable;

    int closestSize = Integer.MAX_VALUE;
    for (var size : sizeByPath.values()) {
      if (size > diskRequired) {
        closestSize = min(closestSize, size);
      }
    }
    return closestSize;
  }

  private Map<String, Integer> parseSizeByPath(String input) {
    Stack<String> path = new Stack<>();
    Map<String, Integer> sizeByPath = new HashMap<>();

    for (var line : getLines(input)) {
      var words = line.trim().split(SPACE);
      if (words[1].equals("cd")) {
        if (words[2].equals("..")) {
          path.pop();
        } else {
          path.push(words[2]);
        }
      } else if (!words[1].equals("ls")) {
        if (!words[0].equals("dir")) {
          var size = parseInt(words[0]);
          var pathCopy = (Stack<String>) path.clone();
          do {
            var pathString = pathCopy.toString();
            sizeByPath.compute(pathString, (k, v) -> (v == null) ? size : v + size);
            pathCopy.pop();
          } while (!pathCopy.isEmpty());
        }
      }
    }

    return sizeByPath;
  }

}
