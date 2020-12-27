package com.radupetre.adventofcode.year2020.day20;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.reverse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Tile {

  private static final String TILE_ID_SEPARATOR = " ";
  private static final String TILE_ID_END = ":";

  final Integer tileId;
  final Set<Integer> borderValues;

  Tile(String tileString) {
    final List<String> tileLines = getLines(tileString);

    // extract tileId from first line
    this.tileId = parseInt(
        tileLines.get(0)
            .split(TILE_ID_SEPARATOR)[1]
            .replace(TILE_ID_END, "")
            .trim());

    // extract the borders
    String topBorder = tileLines.get(1);
    String bottomBorder = tileLines.get(tileLines.size() - 1);
    String leftBorder = extractColumn(0, tileLines);
    String rightBorder = extractColumn(tileLines.get(1).length() - 1, tileLines);

    this.borderValues = Stream
        .of(topBorder, reverse(topBorder), bottomBorder, reverse(bottomBorder),
            leftBorder, reverse(leftBorder), rightBorder, reverse(rightBorder))
        .map(this::convertBorderToBinary)
        .collect(Collectors.toSet());
  }

  private Integer convertBorderToBinary(String stringBorder) {
    // all borders can be converted to binary int
    String stringBinary = stringBorder
        .replace("#", "1")
        .replace(".", "0");

    return parseInt(stringBinary, 2);
  }

  private String extractColumn(int columnPosition, List<String> tileLines) {
    return IntStream.range(1, tileLines.size())
        .mapToObj(tileLines::get)
        .map(tileLine -> tileLine.charAt(columnPosition))
        .collect(Collector.of(
            StringBuilder::new,
            StringBuilder::append,
            StringBuilder::append,
            StringBuilder::toString)
        );
  }
}
