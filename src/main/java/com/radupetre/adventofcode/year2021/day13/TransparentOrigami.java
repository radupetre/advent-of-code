package com.radupetre.adventofcode.year2021.day13;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.EMPTY_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.EQUALS;
import static com.radupetre.adventofcode.utils.StringUtility.NEW_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2021.day13.FoldType.X_FOLD;
import static com.radupetre.adventofcode.year2021.day13.FoldType.Y_FOLD;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/13
 */
@Service
@Log4j2
public class TransparentOrigami extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 13);
  }

  @Override
  public Object solvePart1(String input) {
    final List<String> dotsAndFolds = StringUtility.getBatches(input, EMPTY_LINE);

    Set<Dot> dots = parseDots(dotsAndFolds.get(0));
    List<Fold> folds = parseFolds(dotsAndFolds.get(1));

    doFold(dots, folds.get(0));
    return dots.size();
  }

  @Override
  public Object solvePart2(String input) {
    final List<String> dotsAndFolds = StringUtility.getBatches(input, EMPTY_LINE);

    Set<Dot> dots = parseDots(dotsAndFolds.get(0));
    List<Fold> folds = parseFolds(dotsAndFolds.get(1));

    folds.forEach(fold -> doFold(dots, fold));
    return readableOutput(dots, folds);
  }

  private void doFold(Set<Dot> dots, Fold fold) {
    Set<Dot> unfoldedDots = dots.stream()
        .filter(dot -> fold.isXFold() ? dot.x >= fold.foldLine : dot.y >= fold.foldLine)
        .collect(toSet());
    Set<Dot> foldedDots = foldDots(unfoldedDots, fold);

    dots.removeAll(unfoldedDots);
    dots.addAll(foldedDots);
  }

  private Set<Dot> foldDots(Set<Dot> unfoldedDots, Fold fold) {
    return unfoldedDots.stream()
        .filter(dot -> fold.isXFold() ? dot.x > fold.foldLine : dot.y > fold.foldLine)
        .map(dot -> fold.isXFold()
            ? new Dot((fold.foldLine * 2) - dot.x, dot.y)
            : new Dot(dot.x, (fold.foldLine * 2) - dot.y))
        .collect(toSet());
  }

  private Set<Dot> parseDots(String dotsInput) {
    return getLines(dotsInput)
        .stream()
        .map(Dot::new)
        .collect(toSet());
  }

  private List<Fold> parseFolds(String foldsInput) {
    return getLines(foldsInput.trim()).stream()
        .map(Fold::new)
        .collect(toList());
  }

  private String readableOutput(Set<Dot> dots, List<Fold> folds) {
    int maxX = folds.stream().filter(Fold::isXFold).map(Fold::getFoldLine).min(Integer::compare)
        .get();
    int maxY = folds.stream().filter(Fold::isYFold).map(Fold::getFoldLine).min(Integer::compare)
        .get();

    StringBuilder output = new StringBuilder();
    for (int y = 0; y < maxY; y++) {
      for (int x = 0; x < maxX; x++) {
        if (dots.contains(new Dot(x, y))) {
          output.append("█");
        } else {
          output.append(" ");
        }
      }
      output.append(NEW_LINE);
    }

    return output.toString();
  }
}

@EqualsAndHashCode
@RequiredArgsConstructor
class Dot {

  final int x, y;

  Dot(String dotString) {
    final String[] dotCoordinates = dotString.split(COMMA);
    x = parseInt(dotCoordinates[0]);
    y = parseInt(dotCoordinates[1]);
  }
}

@Getter
@RequiredArgsConstructor
class Fold {

  final FoldType foldType;
  final int foldLine;

  boolean isXFold() {
    return X_FOLD == foldType;
  }

  boolean isYFold() {
    return !isXFold();
  }

  Fold(String foldString) {
    foldType = foldString.contains("x") ? X_FOLD : Y_FOLD;
    foldLine = parseInt(foldString.split(EQUALS)[1]);
  }
}

enum FoldType {
  X_FOLD, Y_FOLD
}
