package com.radupetre.adventofcode.year2020.day16;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;

class Attribute {

  private static final String ATTRIBUTE_SEPARATOR = ":";
  private static final String RANGE_SEPARATOR = "or";

  final String name;
  final List<Range> ranges;

  Attribute(String attributeString) {
    final String[] attributeParts = attributeString.split(ATTRIBUTE_SEPARATOR);
    this.name = attributeParts[0];
    this.ranges = stream(attributeParts[1].split(RANGE_SEPARATOR))
        .map(Range::new)
        .collect(toList());
  }
}
