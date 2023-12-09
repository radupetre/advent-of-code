package com.radupetre.adventofcode.year2023.day03;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Character.isDigit;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Solving of https://adventofcode.com/2023/day/3
 */
@Service
@Log4j2
public class GearRatios extends AbstractAdventSolution {

    @Override
    public SolveContext getSolveContext() {
        return new SolveContext(2023, 3);
    }

    int maxL;
    int maxC;
    char[][] schematic;
    Map<String, List<Integer>> gears;


    @Override
    public Object solvePart1(String input) {
        parseSchematic(input);

        int sum = 0;
        for (int l = 0; l < maxL; l++) {
            int number = 0;
            int digits = 0;
            for (int c = 0; c < maxC; c++) {
                if (isDigit(schematic[l][c])) {
                    number = number * 10 + schematic[l][c] - '0';
                    digits++;
                } else if (number > 0) {
                    if (isEnginePart(l, c, digits)) {
                        sum += number;
                    }
                    number = 0;
                    digits = 0;
                }
            }
            if (isEnginePart(l, maxC, digits)) {
                sum += number;
            }
        }

        return sum;
    }

    @Override
    public Object solvePart2(String input) {
        parseSchematic(input);

        for (int l = 0; l < maxL; l++) {
            int number = 0;
            int digits = 0;
            for (int c = 0; c < maxC; c++) {
                if (isDigit(schematic[l][c])) {
                    number = number * 10 + schematic[l][c] - '0';
                    digits++;
                } else if (number > 0) {
                    if (isEnginePart(l, c, digits)) {
                        for (String gear : getGears(l, c, digits)) {
                            gears.computeIfAbsent(gear, __ -> new ArrayList<>());
                            gears.get(gear).add(number);
                        }
                    }

                    number = 0;
                    digits = 0;
                }
            }
            if (isEnginePart(l, maxC, digits)) {
                for (String gear : getGears(l, maxC, digits)) {
                    gears.computeIfAbsent(gear, __ -> new ArrayList<>());
                    gears.get(gear).add(number);
                }
            }
        }

        return gears.values().stream()
                .filter(gears -> gears.size() == 2)
                .map(gears -> gears.get(0) * gears.get(1))
                .reduce(0, Integer::sum);
    }

    public void parseSchematic(String input) {
        gears = new HashMap<>();
        List<String> lines = getLines(input);
        maxL = lines.size();
        maxC = lines.get(0).length();
        schematic = new char[maxL][maxC];

        for (int l = 0; l < maxL; l++) {
            for (int c = 0; c < maxC; c++) {
                schematic[l][c] = lines.get(l).charAt(c);
            }
        }
    }

    private boolean isEnginePart(int l, int c, int digits) {
        for (int ll = max(l - 1, 0); ll <= min(l + 1, maxL - 1); ll++) {
            for (int cc = max(c - digits - 1, 0); cc <= min(c, maxC - 1); cc++) {
                char neighbour = schematic[ll][cc];
                if (isDigit(neighbour)) continue;
                if (neighbour == '.') continue;
                return true;
            }
        }
        return false;
    }

    private List<String> getGears(int l, int c, int digits) {
        List<String> gears = new ArrayList<>();
        for (int ll = max(l - 1, 0); ll <= min(l + 1, maxL - 1); ll++) {
            for (int cc = max(c - digits - 1, 0); cc <= min(c, maxC - 1); cc++) {
                if (schematic[ll][cc] == '*') {
                    gears.add(ll + " " + cc);
                }
            }
        }
        return gears;
    }
}
