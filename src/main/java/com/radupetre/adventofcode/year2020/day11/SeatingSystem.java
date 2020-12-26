package com.radupetre.adventofcode.year2020.day11;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/11
 */
@Service
@Log4j2
public class SeatingSystem extends AbstractAdventSolution {

  private static final char NO_SEAT = '.';
  private static final char EMPTY_SEAT = 'L';
  private static final char OCCUPIED_SEAT = '#';

  private static final int[][] NEIGHBOUR_COORDINATES = {
      {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
  };

  private static final boolean ADJACENT_NEIGHBOURS = true;
  private static final boolean VISIBLE_NEIGHBOURS = false;

  private static final int FOUR = 4;
  private static final int FIVE = 5;

  private static final int ROW = 0;
  private static final int COLUMN = 1;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 11);
  }

  @Override
  public Object solvePart1(String seatPlanString) {
    SeatPlan seatPlan = new SeatPlan(seatPlanString);

    return getOccupiedSeatsAfterMoving(seatPlan, ADJACENT_NEIGHBOURS, FOUR);
  }

  @Override
  public Object solvePart2(String seatPlanString) {
    SeatPlan seatPlan = new SeatPlan(seatPlanString);

    return getOccupiedSeatsAfterMoving(seatPlan, VISIBLE_NEIGHBOURS, FIVE);
  }

  private long getOccupiedSeatsAfterMoving(SeatPlan masterSeatPlan, boolean onlyAdjacent,
      int occupyTolerance) {
    SeatPlan seatPlan = masterSeatPlan.copy();

    while (hasExecutedValidMoves(seatPlan, onlyAdjacent, occupyTolerance)) {
      // keep executing moves
    }

    return countOccupiedSeats(seatPlan);
  }

  private boolean hasExecutedValidMoves(SeatPlan seatPlan, boolean onlyAdjacent,
      int occupyTolerance) {
    boolean hasExecutedValidMoves = false;
    char[][] seatsAtNextStep = seatPlan.copySeats();

    for (int row = 0; row < seatPlan.rows; row++) {
      for (int column = 0; column < seatPlan.columns; column++) {

        char nextSeatStatus = getNextSeatStatus(row, column, seatPlan, onlyAdjacent,
            occupyTolerance);
        char currentSeatStatus = seatPlan.seats[row][column];

        if (nextSeatStatus != currentSeatStatus) {
          hasExecutedValidMoves = true;
          seatsAtNextStep[row][column] = nextSeatStatus;
        }
      }
    }

    seatPlan.updateSeating(seatsAtNextStep);
    return hasExecutedValidMoves;
  }

  private char getNextSeatStatus(int row, int column, SeatPlan seatPlan, boolean onlyAdjacent,
      int occupyTolerance) {
    char currentSeatStatus = seatPlan.seats[row][column];

    if (NO_SEAT == currentSeatStatus) {
      // no seat remains no seat
      return currentSeatStatus;
    }

    int neighboursCount = countNeighbours(row, column, seatPlan, onlyAdjacent);

    switch (seatPlan.seats[row][column]) {
      case EMPTY_SEAT:
        if (neighboursCount == 0) {
          // empty seat with no occupied neighbours becomes occupied
          return OCCUPIED_SEAT;
        } else {
          // empty seat with occupied neighbours stays empty;
          return EMPTY_SEAT;
        }
      case OCCUPIED_SEAT:
        if (neighboursCount >= occupyTolerance) {
          // occupied seat with >=4 occupied neighbours becomes empty
          return EMPTY_SEAT;
        } else {
          // occupied seat with <4 occupied neighbours stays occupied;
          return OCCUPIED_SEAT;
        }
      default:
        return currentSeatStatus;
    }
  }

  private int countNeighbours(int row, int column, SeatPlan seatPlan,
      boolean onlyAdjacent) {
    List<Character> neighbours = onlyAdjacent
        ? getAdjacentNeighbours(row, column, seatPlan)
        : getVisibleNeighbours(row, column, seatPlan);

    return (int) neighbours.stream()
        .filter(neighbourStatus -> OCCUPIED_SEAT == neighbourStatus)
        .count();
  }

  private List<Character> getVisibleNeighbours(int row, int column, SeatPlan seatPlan) {
    return Arrays.stream(NEIGHBOUR_COORDINATES)
        .map(direction -> getFirstNeighbourInDirection(direction, row, column, seatPlan))
        .filter(Objects::nonNull)
        .collect(toList());
  }

  private Character getFirstNeighbourInDirection(int[] direction, int row, int column,
      SeatPlan seatPlan) {
    int nextRow = row + direction[ROW];
    int nextColumn = column + direction[COLUMN];

    while (isSeatInRange(nextRow, nextColumn, seatPlan.rows, seatPlan.columns)) {
      Character seatStatus = seatPlan.seats[nextRow][nextColumn];
      if (seatStatus == NO_SEAT) {
        // still haven't found a seat, move forward in the same direction
        nextRow += direction[ROW];
        nextColumn += direction[COLUMN];
      } else {
        // found a seat in the wanted direction
        return seatStatus;
      }
    }

    // reached the end of direction without finding a seat
    return NO_SEAT;
  }

  private boolean isSeatInRange(int nextRow, int nextColumn, int rows, int columns) {
    return nextRow >= 0
        && nextRow < rows
        && nextColumn >= 0
        && nextColumn < columns;
  }

  private List<Character> getAdjacentNeighbours(int row, int column, SeatPlan seatPlan) {
    return Arrays.stream(NEIGHBOUR_COORDINATES)
        .filter(neighbourAdjustment -> isSeatInRange(neighbourAdjustment[ROW] + row,
            neighbourAdjustment[COLUMN] + column, seatPlan.rows, seatPlan.columns))
        .map(neighbourAdjustment ->
            seatPlan.seats[neighbourAdjustment[ROW] + row][neighbourAdjustment[COLUMN] + column])
        .collect(toList());
  }

  private long countOccupiedSeats(SeatPlan seatPlan) {
    return stream(seatPlan.seats)
        .flatMapToInt(seatsRow -> CharBuffer.wrap(seatsRow).chars())
        .filter(seatStatus -> seatStatus == OCCUPIED_SEAT)
        .count();
  }
}

@AllArgsConstructor()
class SeatPlan {

  final int rows;
  final int columns;
  char[][] seats;

  SeatPlan(String seatingPlanString) {
    final List<String> seatingLines = StringUtility.getLines(seatingPlanString);

    this.rows = seatingLines.size();
    this.columns = seatingLines.get(0).length();

    this.seats = new char[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        seats[row][column] = seatingLines.get(row).charAt(column);
      }
    }
  }

  public void updateSeating(char[][] seatsAtNextStep) {
    seats = seatsAtNextStep;
  }

  SeatPlan copy() {
    return new SeatPlan(this.rows, this.columns, copySeats());
  }

  char[][] copySeats() {
    return Arrays.stream(this.seats)
        .map(char[]::clone)
        .toArray(char[][]::new);
  }
}
