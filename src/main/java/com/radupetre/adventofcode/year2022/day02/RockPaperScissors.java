package com.radupetre.adventofcode.year2022.day02;

import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static com.radupetre.adventofcode.year2022.day02.RockPaperScissors.HandShape.PAPER;
import static com.radupetre.adventofcode.year2022.day02.RockPaperScissors.HandShape.ROCK;
import static com.radupetre.adventofcode.year2022.day02.RockPaperScissors.HandShape.SCISSOR;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/2
 */
@Service
@Log4j2
public class RockPaperScissors extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 2);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .map(this::getHandShapesFromNotation)
        .mapToInt(this::getScore)
        .sum();
  }

  @Override
  public Object solvePart2(String input) {
    return getLinesAsStream(input)
        .map(this::getHandShapesFromInstruction)
        .mapToInt(this::getScore)
        .sum();
  }

  private Pair<HandShape, HandShape> getHandShapesFromNotation(String line) {
    final String[] input = line.split(SPACE);
    char theirInput = input[0].charAt(0);
    char ourInput = input[1].charAt(0);

    HandShape theirs = HandShape.values()[theirInput - 'A'];
    HandShape ours = HandShape.values()[ourInput - 'X'];
    return Pair.of(theirs, ours);
  }

  private Pair<HandShape, HandShape> getHandShapesFromInstruction(String line) {
    final String[] input = line.split(SPACE);
    char theirInput = input[0].charAt(0);
    char ourInput = input[1].charAt(0);

    switch (ourInput) {
      case 'X' -> ourInput = (char) (theirInput - 1);
      case 'Y' -> ourInput = theirInput;
      case 'Z' -> ourInput = (char) (theirInput + 1);
    }
    if (ourInput < 'A')  ourInput += 3;
    if (ourInput > 'C')  ourInput -= 3;

    HandShape theirs = HandShape.values()[theirInput - 'A'];
    HandShape ours = HandShape.values()[ourInput - 'A'];
    return Pair.of(theirs, ours);
  }

  private int getScore(Pair<HandShape, HandShape> handShapes) {
    return getShapeScore(handShapes) + getOutcomeScore(handShapes);
  }

  private int getOutcomeScore(Pair<HandShape, HandShape> handShapes) {
    var theirs = handShapes.getLeft();
    var ours = handShapes.getRight();

    if ((ours == ROCK && theirs == PAPER) ||
        (ours == PAPER && theirs == SCISSOR) ||
        (ours == SCISSOR && theirs == ROCK)
    ) {
      return 0;
    } else if (ours.equals(theirs)) {
      return 3;
    } else {
      return 6;
    }
  }

  private int getShapeScore(Pair<HandShape, HandShape> handShapes) {
    var ours = handShapes.getRight();
    return switch (ours) {
      case ROCK -> 1;
      case PAPER -> 2;
      case SCISSOR -> 3;
    };
  }

  public enum HandShape {
    ROCK, PAPER, SCISSOR
  }
}
