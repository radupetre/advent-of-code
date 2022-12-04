package com.radupetre.adventofcode.year2022.day04;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.DASH;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/4
 */
@Service
@Log4j2
public class CampCleanup extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 4);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .map(this::asRanges)
        .filter(rangePair -> {
          var first = rangePair.getLeft();
          var second = rangePair.getRight();
          return first.contains(second) || second.contains(first);
        })
        .count();
  }

  @Override
  public Object solvePart2(String input) {
    return getLinesAsStream(input)
        .map(this::asRanges)
        .filter(rangePair -> {
          var first = rangePair.getLeft();
          var second = rangePair.getRight();
          return first.overlaps(second);
        })
        .count();
  }

  private Pair<Range, Range> asRanges(String input) {
    final List<Range> ranges = getBatchesAsStream(input, COMMA)
        .map(Range::new)
        .collect(toList());

    return Pair.of(ranges.get(0), ranges.get(1));
  }

  class Range {

    private final int start, end;

    Range(String range) {
      var rangeSegments = getBatchesAsStream(range, DASH)
          .map(Integer::valueOf)
          .collect(toList());
      start = rangeSegments.get(0);
      end = rangeSegments.get(1);
    }

    boolean contains(Range other) {
      return this.start <= other.start && other.end <= this.end;
    }

    boolean overlaps(Range other) {
      return this.start <= other.end && other.start <= this.end;
    }
  }
}
