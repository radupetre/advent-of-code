package com.radupetre.adventofcode.year2020.day20;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.reverse;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileBuilder {

  private static final String TILE_ID_SEPARATOR = " ";
  private static final String TILE_ID_END = ":";

  static Tile build(String tileString) {
    final List<String> tileLines = getLines(tileString);

    // extract tileId from first line
    Integer tileId = parseInt(
        tileLines.get(0)
            .split(TILE_ID_SEPARATOR)[1]
            .replace(TILE_ID_END, "")
            .trim());

    // extract the tile center for later use
    boolean[][] tileCenter = extractTileCenter(tileLines);

    // extract clockwise/reverse borders
    String topClockwiseString = tileLines.get(1);
    String topReverseString = reverse(topClockwiseString);
    String rightClockwiseString = extractColumn(tileLines.get(1).length() - 1, tileLines);
    String rightReverseString = reverse(rightClockwiseString);
    String bottomReverseString = tileLines.get(tileLines.size() - 1);
    String bottomClockwiseString = reverse(bottomReverseString);
    String leftReverseString = extractColumn(0, tileLines);
    String leftClockwiseString = reverse(leftReverseString);

    List<Integer> clockwiseBorders = of(topClockwiseString, rightClockwiseString,
        bottomClockwiseString,
        leftClockwiseString)
        .map(TileBuilder::convertBorderToBinary)
        .collect(toList());
    List<Integer> reverseBorders = of(topReverseString, rightReverseString, bottomReverseString,
        leftReverseString)
        .map(TileBuilder::convertBorderToBinary)
        .collect(toList());

    Set<Integer> borderValues = Stream.of(clockwiseBorders, reverseBorders)
        .flatMap(Collection::stream)
        .collect(toSet());

    return new Tile(tileId, tileString, tileCenter, borderValues, clockwiseBorders, reverseBorders);
  }

  private static String extractColumn(int columnPosition, List<String> tileLines) {
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

  private static boolean[][] extractTileCenter(List<String> tileLines) {
    boolean[][] tileCenter = new boolean[8][8];

    for (int x = 0; x < 8; x++) {
      String line = tileLines.get(x + 2);
      for (int y = 0; y < 8; y++) {
        char letter = line.charAt(y + 1);

        tileCenter[x][y] = letter == '#';
      }
    }

    return tileCenter;
  }

  private static Integer convertBorderToBinary(String stringBorder) {
    // all borders can be converted to binary int
    String stringBinary = stringBorder
        .replace("#", "1")
        .replace(".", "0");

    return parseInt(stringBinary, 2);
  }
}
