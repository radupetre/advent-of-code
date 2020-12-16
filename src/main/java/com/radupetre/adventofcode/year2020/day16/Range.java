package com.radupetre.adventofcode.year2020.day16;

import static java.lang.Integer.compare;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Range implements Comparable<Range> {

  private static final String LOW_HIGH_SEPARATOR = "-";

  final int low;
  final int high;

  Range(String rangeString) {
    final String[] rangeParts = rangeString.split(LOW_HIGH_SEPARATOR);
    this.low = parseInt(rangeParts[0].trim());
    this.high = parseInt(rangeParts[1].trim());
  }

  @Override
  public int compareTo(Range otherRange) {
    int lowCompare = compare(this.low, otherRange.low);
    if (lowCompare == 0) {
      return compare(this.high, otherRange.high);
    } else {
      return lowCompare;
    }
  }

  public boolean hasOverlap(Range otherRange) {
    return this.low <= otherRange.low && otherRange.low <= this.high;
  }

  public Range merge(Range otherRange) {
    return new Range(min(this.low, otherRange.low), max(this.high, otherRange.high));
  }
}
