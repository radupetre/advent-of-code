package com.radupetre.adventofcode.solution;

import static java.lang.Integer.compare;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class SolveContext implements Comparable<SolveContext> {

  private final int year;
  private final int day;

  @Override
  public int compareTo(SolveContext o) {
    final int yearCompare = compare(this.year, o.year);
    if (yearCompare == 0) {
      return compare(this.day, o.day);
    } else {
      return yearCompare;
    }
  }
}
