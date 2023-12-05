package com.radupetre.adventofcode.year2023.day01;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.lang.Character.isDigit;
import static java.lang.Math.min;

/**
 * Solving of https://adventofcode.com/2023/day/1
 */
@Service
@Log4j2
public class Trebuchet extends AbstractAdventSolution {

    @Override
    public SolveContext getSolveContext() {
        return new SolveContext(2023, 1);
    }

    private static final Map<String, Integer> digits = Map.of("one", 1, "two", 2,
            "three", 3, "four", 4, "five", 5, "six", 6,
            "seven", 7, "eight", 8, "nine", 9, "zero", 0);

    @Override
    public Object solvePart1(String input) {
        return getLinesAsStream(input)
                .map(this::parseDigits)
                .reduce(0, Integer::sum);
    }

    @Override
    public Object solvePart2(String input) {
        return getLinesAsStream(input)
                .map(this::parseDigitsAndWords)
                .reduce(0, Integer::sum);
    }

    private int parseDigits(String text) {
        return getDigits(text, false);
    }

    private int parseDigitsAndWords(String text) {
        return getDigits(text, true);
    }

    private int getDigits(String text, boolean includeWords) {
        int firstDigit = 0;
        outer:
        for (int pos = 0; pos < text.length(); pos++) {
            if (isDigit(text.charAt(pos))) {
                firstDigit = text.charAt(pos) - '0';
                break;
            }
            if (includeWords) {
                String word = text.substring(pos, min(pos + 5, text.length() - 1));
                for (var digit : digits.entrySet()) {
                    if (word.startsWith(digit.getKey())) {
                        firstDigit = digit.getValue();
                        break outer;
                    }
                }
            }
        }

        int lastDigit = 0;
        outer:
        for (int pos = text.length() - 1; pos >= 0; pos--) {
            if (isDigit(text.charAt(pos))) {
                lastDigit = text.charAt(pos) - '0';
                break;
            }
            if (includeWords) {
                String word = text.substring(pos, min(pos + 5, text.length()));
                for (var digit : digits.entrySet()) {
                    if (word.startsWith(digit.getKey())) {
                        lastDigit = digit.getValue();
                        break outer;
                    }
                }
            }
        }

        return firstDigit * 10 + lastDigit;
    }

}
