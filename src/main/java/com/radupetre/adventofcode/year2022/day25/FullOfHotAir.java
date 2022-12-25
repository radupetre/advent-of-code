package com.radupetre.adventofcode.year2022.day25;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/25
 */
@Service
@Log4j2
public class FullOfHotAir extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 25);
  }

  @Override
  public Object solvePart1(String input) {
    return getLinesAsStream(input)
        .reduce(this::addSnafu)
        .orElseThrow();
  }

  @Override
  public Object solvePart2(String input) {
    return null;
  }

  private String addSnafu(String number1, String number2) {
    StringBuilder result = new StringBuilder();
    String carry = "";
    while (!number1.isEmpty() || !number2.isEmpty() || !carry.isEmpty()) {
      char digit1 = getLastChar(number1);
      char digit2 = getLastChar(number2);
      number1 = removeLastChar(number1);
      number2 = removeLastChar(number2);

      var sum = addSnafu(digit1, digit2);
      carry = carry.isEmpty()
          ? sum
          : addSnafu(carry, sum);
      result.append(getLastChar(carry));
      carry = removeLastChar(carry);
    }
    return result.reverse().toString();
  }

  private String addSnafu(char digit1, char digit2) {
    return switch (digit1) {
      case '='-> switch (digit2) {
        case '=' -> "-1";
        case '-' -> "-2";
        case '0' -> "=";
        case '1' -> "-";
        case '2' -> "0";
        default -> throw new RuntimeException();
      };
      case '-'-> switch (digit2) {
        case '=' -> "-2";
        case '-' -> "=";
        case '0' -> "-";
        case '1' -> "0";
        case '2' -> "1";
        default -> throw new RuntimeException();
      };
      case '0'-> switch (digit2) {
        case '=' -> "=";
        case '-' -> "-";
        case '0' -> "0";
        case '1' -> "1";
        case '2' -> "2";
        default -> throw new RuntimeException();
      };
      case '1'-> switch (digit2) {
        case '=' -> "-";
        case '-' -> "0";
        case '0' -> "1";
        case '1' -> "2";
        case '2' -> "1=";
        default -> throw new RuntimeException();
      };
      case '2'-> switch (digit2) {
        case '=' -> "0";
        case '-' -> "1";
        case '0' -> "2";
        case '1' -> "1=";
        case '2' -> "1-";
        default -> throw new RuntimeException();
      };
      default -> throw new RuntimeException();
    };
  }

  private String removeLastChar(String s) {
    return (s == null || s.length() == 0) ? "" : (s.substring(0, s.length() - 1));
  }

  private Character getLastChar(String s) {
    return (s == null || s.length() == 0) ? '0' : s.charAt(s.length() - 1);
  }

}