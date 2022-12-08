package com.radupetre.adventofcode.year2022.day08;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/8
 */
@Service
@Log4j2
public class TreetopTreeHouse extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 8);
  }

  int rows;
  int cols;
  int map[][] = new int[100][100];

  @Override
  public Object solvePart1(String input) {
    parseMap(input);

    int visibleCount = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (isEdge(row, col)
            || isVisibleLeft(row, col)
            || isVisibleRight(row, col)
            || isVisibleTop(row, col)
            || isVisibleBottom(row, col)) {
          visibleCount++;
        }
      }
    }

    return visibleCount;
  }

  @Override
  public Object solvePart2(String input) {
    parseMap(input);

    int maxScenicScore = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (isEdge(row, col)) {
          continue;
        }

        int scenicScore = getScoreLeft(row, col) * getScoreRight(row, col) * getScoreTop(row, col) * getScoreBottom(row, col);
        if (scenicScore > maxScenicScore) {
          maxScenicScore = scenicScore;
        }
      }
    }

    return maxScenicScore;
  }

  private void parseMap(String input) {
    var lines = getLines(input);
    rows = lines.size();
    cols = lines.get(0).length();

    var i = lines.iterator();
    for (int row = 0; row < rows; row++) {
      var line = i.next().trim();
      for (int col = 0; col < cols; col++) {
        map[row][col] = Integer.parseInt(String.valueOf(line.charAt(col)));
      }
    }
  }

  private boolean isEdge(int row, int col) {
    return row == 0 || col == 0 || row == rows - 1 || col == cols - 1;
  }

  private boolean isVisibleTop(int row, int col) {
    for (int topRow = row - 1; topRow >= 0; topRow--) {
      if (map[topRow][col] >= map[row][col]) {
        return false;
      }
    }
    return true;
  }

  private boolean isVisibleBottom(int row, int col) {
    for (int bottomRow = row + 1; bottomRow < rows; bottomRow++) {
      if (map[bottomRow][col] >= map[row][col]) {
        return false;
      }
    }
    return true;
  }

  private boolean isVisibleLeft(int row, int col) {
    for (int leftCol = col - 1; leftCol >= 0; leftCol--) {
      if (map[row][leftCol] >= map[row][col]) {
        return false;
      }
    }
    return true;
  }

  private boolean isVisibleRight(int row, int col) {
    for (int rightCol = col + 1; rightCol < rows; rightCol++) {
      if (map[row][rightCol] >= map[row][col]) {
        return false;
      }
    }
    return true;
  }

  private int getScoreTop(int row, int col) {
    int count = 0;
    for (int topRow = row - 1; topRow >= 0; topRow--) {
      count++;
      if (map[topRow][col] >= map[row][col]) {
        break;
      }
    }
    return count;
  }

  private int getScoreBottom(int row, int col) {
    int count = 0;
    for (int bottomRow = row + 1; bottomRow < rows; bottomRow++) {
      count++;
      if (map[bottomRow][col] >= map[row][col]) {
        break;
      }
    }
    return count;
  }

  private int getScoreLeft(int row, int col) {
    int count = 0;
    for (int leftCol = col - 1; leftCol >= 0; leftCol--) {
      count++;
      if (map[row][leftCol] >= map[row][col]) {
        break;
      }
    }
    return count;
  }

  private int getScoreRight(int row, int col) {
    int count = 0;
    for (int rightCol = col + 1; rightCol < rows; rightCol++) {
      count++;
      if (map[row][rightCol] >= map[row][col]) {
        break;
      }
    }
    return count;
  }

}
