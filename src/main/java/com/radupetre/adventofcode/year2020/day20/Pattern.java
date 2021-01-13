package com.radupetre.adventofcode.year2020.day20;

import com.radupetre.adventofcode.utils.StringUtility;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@RequiredArgsConstructor
class Pattern {

  final int x;
  final int y;
  final Set<Pair<Integer, Integer>> points;

  private static String MONSTER_PATTERN =
      "                  # \n"
          + "#    ##    ##    ###\n"
          + " #  #  #  #  #  #   ";

  static Pattern getMonsterPattern() {
    final List<String> lines = StringUtility.getLines(MONSTER_PATTERN);
    final Set<Pair<Integer, Integer>> points = new HashSet<>();
    int maxX = lines.size(), maxY = 0;

    for (int x = 0; x < lines.size(); x++) {
      String line = lines.get(x);
      maxY = Math.max(line.length(), maxY);

      for (int y = 0; y < line.length(); y++) {
        if (line.charAt(y) == '#') {
          points.add(Pair.of(x, y));
        }
      }
    }

    return new Pattern(maxX, maxY, points);
  }
}
