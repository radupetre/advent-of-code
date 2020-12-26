package com.radupetre.adventofcode.year2020.day11;

import com.radupetre.adventofcode.utils.StringUtility;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;

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
