package com.radupetre.adventofcode.year2023.day02;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;

/**
 * Solving of https://adventofcode.com/2023/day/2
 */
@Service
@Log4j2
public class CubeConundrum extends AbstractAdventSolution {

    @Override
    public SolveContext getSolveContext() {
        return new SolveContext(2023, 2);
    }

    @Override
    public Object solvePart1(String input) {
        return getLinesAsStream(input)
                .map(this::parseGame)
                .filter(this::isPossible)
                .map(Game::getNumber)
                .reduce(0, Integer::sum);
    }

    @Override
    public Object solvePart2(String input) {
        return getLinesAsStream(input)
                .map(this::parseGame)
                .map(this::calculatePower)
                .reduce(0, Integer::sum);
    }

    private Integer calculatePower(Game game) {
        return game.rounds.stream().map(Round::getRed).max(Integer::compareTo).orElseThrow()
                * game.rounds.stream().map(Round::getBlue).max(Integer::compareTo).orElseThrow()
                * game.rounds.stream().map(Round::getGreen).max(Integer::compareTo).orElseThrow();
    }

    private boolean isPossible(Game game) {
        for (Round round : game.rounds) {
            if (round.red > 12 || round.green > 13 || round.blue > 14) {
                return false;
            }
        }
        return true;
    }

    private Game parseGame(String input) {
        String[] game = input.split(":");
        int number = parseInt(game[0].split(" ")[1]);

        List<Round> rounds = stream(game[1].split(";"))
                .map(round -> stream(round.split(","))
                        .map(color -> color.trim().split(" "))
                        .collect(Collectors.toMap(color -> color[1], color -> parseInt(color[0]))))
                .map(colorCounts -> new Round(
                        colorCounts.getOrDefault("red", 0),
                        colorCounts.getOrDefault("green", 0),
                        colorCounts.getOrDefault("blue", 0)))
                .collect(Collectors.toList());

        return new Game(number, rounds);
    }

    @RequiredArgsConstructor
    @Getter
    static class Game {
        final int number;
        final List<Round> rounds;
    }

    @RequiredArgsConstructor
    @Getter
    static class Round {
        final int red;
        final int green;
        final int blue;
    }
}
