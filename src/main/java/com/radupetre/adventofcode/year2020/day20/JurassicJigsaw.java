package com.radupetre.adventofcode.year2020.day20;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/20
 */
@Service
@Log4j2
public class JurassicJigsaw extends AbstractAdventSolution {

  private static final String TILE_SEPARATOR = "\n\n";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 20);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Tile> tiles = getBatches(input, TILE_SEPARATOR)
        .stream()
        .map(Tile::new)
        .collect(toList());

    Map<Integer, Set<Integer>> borderToTiles = new HashMap<>();

    // build a map of borders and tiles they belong to
    for (Tile tile : tiles) {
      for (Integer border : tile.borderValues) {
        final Set<Integer> tilesWithSameBorder = borderToTiles
            .computeIfAbsent(border, a -> new HashSet<Integer>());
        tilesWithSameBorder.add(tile.tileId);
      }
    }

    // find tiles with a unique border not shared with other tiles
    final List<Integer> tilesWithUniqueBorder = borderToTiles.entrySet().stream()
        .filter(borderAndTiles -> borderAndTiles.getValue().size() == 1)
        .map(Entry::getValue)
        .flatMap(Collection::stream)
        .collect(toList());

    // count unique borders of each tile
    final Map<Integer, Long> tilesToUniqueBorderCount = tilesWithUniqueBorder.stream()
        .collect(groupingBy(identity(), counting()));

    // corner tiles will have 2 sides * 2 flips = 4 unique borders.
    final List<Integer> cornerTileIds = tilesToUniqueBorderCount.entrySet().stream()
        .filter(entry -> entry.getValue() == 4)
        .map(Entry::getKey)
        .collect(toList());

    // multiply corner ids
    return cornerTileIds.stream()
        .mapToLong(Long::valueOf)
        .reduce(1, Math::multiplyExact);
  }

  @Override
  public Object solvePart2(String input) {
    return null;
  }
}

