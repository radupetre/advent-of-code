package com.radupetre.adventofcode.year2022.day05;

import static com.radupetre.adventofcode.utils.StringUtility.EMPTY_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/5
 */
@Service
@Log4j2
public class SupplyStacks extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 5);
  }

  @Override
  public Object solvePart1(String input) {
    var splitInput = getBatches(input, EMPTY_LINE);
    List<Deque<String>> stacks = parseStacks(splitInput.get(0));

    var instructions = getLines(splitInput.get(1));
    for (var instruction : instructions) {
      var instructionParts = instruction.split(SPACE);
      int steps = parseInt(instructionParts[1]);
      int from = parseInt(instructionParts[3]);
      int to = parseInt(instructionParts[5]);

      for (int step = 0; step < steps; step++) {
        stacks.get(to - 1).push(stacks.get(from - 1).pop());
      }
    }

    return firstFromEachStack(stacks);
  }

  @Override
  public Object solvePart2(String input) {
    var splitInput = getBatches(input, EMPTY_LINE);
    List<Deque<String>> stacks = parseStacks(splitInput.get(0));

    var instructions = getLines(splitInput.get(1));
    for (var instruction : instructions) {
      var instructionParts = instruction.split(SPACE);
      int steps = parseInt(instructionParts[1]);
      int from = parseInt(instructionParts[3]);
      int to = parseInt(instructionParts[5]);

      var temp = new Stack<String>();

      for (int step = 0; step < steps; step++) {
        temp.push(stacks.get(from - 1).pop());
      }

      for (int step = 0; step < steps; step++) {
        stacks.get(to - 1).push(temp.pop());
      }
    }

    return firstFromEachStack(stacks);
  }

  private String firstFromEachStack(List<Deque<String>> stacks) {
    return stacks.stream()
        .filter(not(Deque::isEmpty))
        .map(Deque::pop)
        .collect(joining());
  }

  private List<Deque<String>> parseStacks(String input) {
    var inputLines = getLines(input);
    var positions = countStacks(inputLines.get(inputLines.size() - 1));
    inputLines.remove(inputLines.size() - 1);

    List<Deque<String>> stacks = new ArrayList<>();
    IntStream.range(0, positions).forEach(__ -> stacks.add(new ArrayDeque<>()));

    for (int position = 0; position < positions; position++) {
      for (String stack : inputLines) {
        int crateStart = min(stack.length(), position * 4);
        int crateEnd = min(stack.length(), (position + 1) * 4);
        var crate = stack.substring(crateStart, crateEnd).replaceAll("[^a-zA-Z]", "");
        if (!crate.isBlank()) {
          stacks.get(position).addLast(crate);
        }
      }
    }
    return stacks;
  }

  private int countStacks(String positionsString) {
    Pattern lastNumber = Pattern.compile("(\\d+)(?!.*\\d)");
    Matcher matcher = lastNumber.matcher(positionsString);
    matcher.find();
    return Integer.parseInt(matcher.group(0));
  }

}
