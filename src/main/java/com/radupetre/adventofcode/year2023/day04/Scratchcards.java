package com.radupetre.adventofcode.year2023.day04;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.radupetre.adventofcode.utils.StringUtility.getLinesAsStream;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.counting;

/**
 * Solving of https://adventofcode.com/2023/day/4
 */
@Service
@Log4j2
public class Scratchcards extends AbstractAdventSolution {

    @Override
    public SolveContext getSolveContext() {
        return new SolveContext(2023, 4);
    }


    @Override
    public Object solvePart1(String input) {
        Map<Long, Long> pointCounts = getLinesAsStream(input)
                .map(this::parseCard)
                .map(this::countWinningNumbers)
                .filter(numbers -> numbers > 0)
                .collect(Collectors.groupingBy(identity(), counting()));

        return calculatePoints(pointCounts);
    }

    @Override
    public Object solvePart2(String input) {
        List<Long> cardWinners = getLinesAsStream(input)
                .map(this::parseCard)
                .map(this::countWinningNumbers)
                .collect(Collectors.toList());

        long totalCount = 0;
        long[] cards = new long[cardWinners.size()];
        for (int original = 0; original < cards.length; original++) {
            cards[original] += 1;
            long cardsToCopy = cardWinners.get(original);
            for (int copy = original + 1; copy < original + cardsToCopy + 1; copy++) {
                if (copy < cards.length) {
                    cards[copy] += cards[original];
                }
            }
            totalCount += cards[original];
        }

        return totalCount;
    }

    private Long calculatePoints(Map<Long, Long> pointCounts) {
        long totalPoints = 0;
        long currentPoints = 1;
        long currentNumber = 1;
        while (!pointCounts.isEmpty()) {
            if (pointCounts.containsKey(currentNumber)) {
                totalPoints += currentPoints * pointCounts.get(currentNumber);
                pointCounts.remove(currentNumber);
            }
            currentPoints *= 2;
            currentNumber += 1;
        }
        return totalPoints;
    }

    private long countWinningNumbers(List<Stream<Integer>> numbers) {
        Set<Integer> winningNumber = numbers.get(0).collect(Collectors.toSet());
        List<Integer> cardNumbers = numbers.get(1).collect(Collectors.toList());

        return cardNumbers.stream()
                .filter(winningNumber::contains)
                .count();
    }

    private List<Stream<Integer>> parseCard(String card) {
        String[] numbers = card.split(":")[1].split(Pattern.quote("|"));
        return List.of(parseNumbers(numbers[0]), parseNumbers(numbers[1]));
    }

    private Stream<Integer> parseNumbers(String numbers) {
        return Arrays.stream(numbers.split(" "))
                .filter(not(String::isEmpty))
                .map(Integer::parseInt);
    }

}
