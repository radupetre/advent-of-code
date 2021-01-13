package com.radupetre.adventofcode.year2020.day20;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.year2020.day20.BoardUtils.flipHorizontal;
import static com.radupetre.adventofcode.year2020.day20.BoardUtils.rotateClockwise;
import static com.radupetre.adventofcode.year2020.day20.Side.BOTTOM;
import static com.radupetre.adventofcode.year2020.day20.Side.LEFT;
import static com.radupetre.adventofcode.year2020.day20.Side.RIGHT;
import static com.radupetre.adventofcode.year2020.day20.Side.TOP;
import static com.radupetre.adventofcode.year2020.day20.Transform.UNCHANGED;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/20
 */
@Service
@Log4j2
public class JurassicJigsaw extends AbstractAdventSolution {

  private static final String TILE_SEPARATOR = "\n\n";
  private List<Tile> tiles;
  private Map<Integer, Set<Integer>> borderToTiles;
  private Map<Integer, Tile> tilesById;
  private int tileSize, boardTilesCount, boardSize, centerX, centerY, minX, maxX, minY, maxY;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 20);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Tile> tiles = readTiles(input);
    Map<Integer, Set<Integer>> borderToTiles = getBorderToTiles(tiles);

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

  private List<Tile> readTiles(String input) {
    return getBatches(input, TILE_SEPARATOR)
        .stream()
        .map(TileBuilder::build)
        .collect(toList());
  }

  private HashMap<Integer, Set<Integer>> getBorderToTiles(List<Tile> tiles) {
    HashMap<Integer, Set<Integer>> borderToTiles = new HashMap<>();

    // build a map of borders and tiles they belong to
    for (Tile tile : tiles) {
      for (Integer border : tile.borderValues) {
        final Set<Integer> tilesWithSameBorder = borderToTiles
            .computeIfAbsent(border, a -> new HashSet<>());
        tilesWithSameBorder.add(tile.tileId);
      }
    }
    return borderToTiles;
  }

  @Override
  public Object solvePart2(String input) {
    tiles = readTiles(input);
    borderToTiles = getBorderToTiles(tiles);
    tilesById = tiles.stream().collect(toMap(Tile::getTileId, identity()));

    setSizes();
    TileTransform[][] tilePlacements = getTilePlacements();
    boolean[][] board = composeBoard(tilePlacements);

    Pattern monsterPattern = Pattern.getMonsterPattern();
    Set<Pair<Integer, Integer>> monsterPoints = findPointsInAllTransforms(board, monsterPattern);
    int waterTiles = countWaterTiles(board);

    return waterTiles - monsterPoints.size();
  }

  private int countWaterTiles(boolean[][] board) {
    int waterTiles = 0;
    for (int x = 0; x < boardSize; x++) {
      for (int y = 0; y < boardSize; y++) {
        if (board[x][y]) {
          waterTiles++;
        }
      }
    }
    return waterTiles;
  }

  private void setSizes() {
    boardTilesCount = (int) sqrt(tiles.size());
    tileSize = tiles.get(0).tileCenter.length;
    boardSize = boardTilesCount * tileSize;

    centerX = boardTilesCount + 1;
    centerY = boardTilesCount + 1;

    minX = centerX;
    maxX = centerX;
    minY = centerY;
    maxY = centerY;
  }

  private TileTransform[][] getTilePlacements() {
    // start in the center of a double-sized board
    TileTransform[][] tilePlacements = new TileTransform[(boardTilesCount + 1) * 2][(boardTilesCount
        + 1) * 2];

    // place the first tile
    tilePlacements[centerX][centerY] = TileTransform.of(tiles.get(0), UNCHANGED);

    // fill from the first cell the column above and below
    placeTilesInDirection(TOP, centerX, centerY, tilePlacements);
    placeTilesInDirection(BOTTOM, centerX, centerY, tilePlacements);

    // from the already filled column, start filling in rows
    for (int currentY = minY; currentY <= maxY; currentY++) {
      placeTilesInDirection(RIGHT, centerX, currentY, tilePlacements);
      placeTilesInDirection(LEFT, centerX, currentY, tilePlacements);
    }

    return tilePlacements;
  }

  public void placeTilesInDirection(Side side, int startX, int startY,
      TileTransform[][] tilePlacements) {
    int lastPlacementX = startX;
    int lastPlacementY = startY;

    TileTransform lastPlacement = tilePlacements[lastPlacementX][lastPlacementY];

    TileTransform nextPlacement = getPlacement(lastPlacement, side);
    while (nextPlacement != null) {
      lastPlacement = nextPlacement;
      lastPlacementX += side.xPosition;
      lastPlacementY += side.yPosition;

      tilePlacements[lastPlacementX][lastPlacementY] = lastPlacement;
      nextPlacement = getPlacement(lastPlacement, side);
    }

    minY = min(minY, lastPlacementY);
    maxY = max(maxY, lastPlacementY);
    minX = min(minX, lastPlacementX);
    maxX = max(maxX, lastPlacementX);
  }

  private Set<Pair<Integer, Integer>> findPointsInAllTransforms(boolean[][] board,
      Pattern monsterPattern) {
    List<Function<boolean[][], boolean[][]>> transformations = Arrays.asList(
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize),
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize),
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize),
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize),
        currentBoard -> flipHorizontal(currentBoard, boardSize, boardSize),
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize),
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize),
        currentBoard -> rotateClockwise(currentBoard, boardSize, boardSize)
    );

    // try finding monsters in all transformations
    for (Function<boolean[][], boolean[][]> transformation : transformations) {
      board = transformation.apply(board);
      final Set<Pair<Integer, Integer>> monsterPoints = findPointsInTransform(
          board, monsterPattern);

      if (!monsterPoints.isEmpty()) {
        return monsterPoints;
      }
    }
    return null;
  }

  private Set<Pair<Integer, Integer>> findPointsInTransform(boolean[][] board,
      Pattern monsterPattern) {
    Set<Pair<Integer, Integer>> allMonsterPoints = new HashSet<>();

    for (int x = 0; x < (boardSize) - monsterPattern.x; x++) {
      for (int y = 0; y < (boardSize) - monsterPattern.y; y++) {

        Set<Pair<Integer, Integer>> currentMonsterPoints = new HashSet<>();

        for (Pair<Integer, Integer> monsterPointOffset : monsterPattern.points) {
          int monsterPointX = monsterPointOffset.getLeft() + x;
          int monsterPointY = monsterPointOffset.getRight() + y;

          if (board[monsterPointX][monsterPointY]) {
            currentMonsterPoints.add(Pair.of(monsterPointX, monsterPointY));
          } else {
            break;
          }
        }

        if (currentMonsterPoints.size() == monsterPattern.points.size()) {
          allMonsterPoints.addAll(currentMonsterPoints);
        }
      }
    }

    return allMonsterPoints;
  }

  private boolean[][] composeBoard(TileTransform[][] tilePlacements) {
    boolean[][] board = new boolean[boardTilesCount * tileSize][boardTilesCount * tileSize];

    for (int tileX = 0; tileX < boardTilesCount; tileX++) {
      for (int tileY = 0; tileY < boardTilesCount; tileY++) {

        TileTransform sectionTileTransform = tilePlacements[minX + tileY][minY + tileX];
        boolean[][] boardSection = sectionTileTransform.getBoardSection();

        int offsetX = tileX * tileSize;
        int offsetY = tileY * tileSize;

        for (int sectionX = 0; sectionX < tileSize; sectionX++) {
          for (int sectionY = 0; sectionY < tileSize; sectionY++) {
            board[offsetX + sectionX][offsetY + sectionY] = boardSection[sectionX][sectionY];
          }
        }
      }
    }

    return board;
  }

  private TileTransform getPlacement(TileTransform lastPlacement, Side lastSide) {
    Integer matchingBorder = lastPlacement.getBorder(lastSide);
    final Optional<Tile> matchingTileOptional = borderToTiles.get(matchingBorder)
        .stream()
        .filter(tileId -> lastPlacement.tile.tileId != tileId)
        .map(tilesById::get)
        .findFirst();

    if (matchingTileOptional.isEmpty()) {
      return null;
    }

    Tile matchingTile = matchingTileOptional.get();

    Side oppositeSide = lastSide.getOpposite();
    Transform matchingTransform = matchingTile
        .getTransformMatchingBorder(oppositeSide, matchingBorder);

    return TileTransform.of(matchingTile, matchingTransform);
  }

}

