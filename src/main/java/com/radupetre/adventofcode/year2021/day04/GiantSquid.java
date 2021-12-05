package com.radupetre.adventofcode.year2021.day04;

import static com.radupetre.adventofcode.utils.StringUtility.COMMA;
import static com.radupetre.adventofcode.utils.StringUtility.EMPTY_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.NEW_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.SPACE;
import static com.radupetre.adventofcode.utils.StringUtility.cleanSpaces;
import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/4
 */
@Service
@Log4j2
public class GiantSquid extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 4);
  }

  @Override
  public Object solvePart1(String input) {
    List<String> inputFragments = new LinkedList<>(getBatches(input, EMPTY_LINE));

    final List<Integer> numbers = parseNumbers(getBatches(inputFragments.get(0), COMMA));
    List<BingoBoard> boards = parseBoards(inputFragments);

    return getScoreOfBoardWithRank(numbers, boards, 1);
  }

  @Override
  public Object solvePart2(String input) {
    List<String> inputFragments = new LinkedList<>(getBatches(input, EMPTY_LINE));

    final List<Integer> numbers = parseNumbers(getBatches(inputFragments.get(0), COMMA));
    List<BingoBoard> boards = parseBoards(inputFragments);

    return getScoreOfBoardWithRank(numbers, boards, boards.size());
  }

  private List<Integer> parseNumbers(List<String> batches) {
    return batches
        .stream()
        .map(Integer::valueOf)
        .collect(toList());
  }

  private List<BingoBoard> parseBoards(List<String> boards) {
    boards.remove(0);
    List<BingoBoard> converted = new ArrayList<>();

    for (String board : boards) {
      converted.add(new BingoBoard(board));
    }

    return converted;
  }

  private int getScoreOfBoardWithRank(
      List<Integer> allNumbers,
      List<BingoBoard> boards,
      int wantedBoardRank) {
    Set<Integer> seenNumbers = new HashSet<>();
    int currentBoardRank = 1;

    for (int currentNumber : allNumbers) {
      seenNumbers.add(currentNumber);
      for (BingoBoard currentBoard : boards) {
        if (!currentBoard.hasWon() && currentBoard.winsWithNumbers(seenNumbers)) {
          if (currentBoardRank == wantedBoardRank) {
            return calculateScore(currentBoard, seenNumbers, currentNumber);
          } else {
            currentBoardRank++;
          }
        }
      }
    }

    throw new IllegalStateException("No board found");
  }

  private int calculateScore(BingoBoard board, Set<Integer> seenNumbers,
      int winningNumber) {
    final Integer unusedNumbersSum = board.getUnusedNumbers(seenNumbers)
        .stream()
        .reduce(0, Integer::sum);

    return unusedNumbersSum * winningNumber;
  }

}

class BingoBoard {

  private final int[][] board;
  private int boardSize;
  private boolean hasWon;

  public BingoBoard(String input) {
    final List<String> lines = getBatches(input, NEW_LINE);
    boardSize = lines.size();
    board = new int[boardSize][boardSize];

    for (int line = 0; line < boardSize; line++) {
      List<String> numbers = getBatches(cleanSpaces(lines.get(line)), SPACE);
      for (int column = 0; column < boardSize; column++) {
        board[line][column] = parseInt(numbers.get(column));
      }
    }
  }

  public boolean hasWon() {
    return hasWon;
  }

  public boolean winsWithNumbers(Set<Integer> seenNumbers) {
    // iterate lines and columns at the same time
    for (int x = 0; x < boardSize; x++) {
      Set<Integer> lineNumbers = new HashSet<>();
      Set<Integer> columnNumbers = new HashSet<>();

      for (int y = 0; y < boardSize; y++) {
        // (x,y) gives us line numbers while (y,x) gives us column numbers
        lineNumbers.add(board[x][y]);
        columnNumbers.add(board[y][x]);
      }

      // if a line or column is complete
      if (seenNumbers.containsAll(lineNumbers) || seenNumbers.containsAll(columnNumbers)) {
        hasWon = true;
        return true;
      }
    }

    return false;
  }

  public Collection<Integer> getUnusedNumbers(Set<Integer> seenNumbers) {
    final Set<Integer> boardNumbers = Stream.of(board)
        .flatMapToInt(IntStream::of)
        .boxed()
        .collect(toSet());

    boardNumbers.removeAll(seenNumbers);
    return boardNumbers;
  }
}

