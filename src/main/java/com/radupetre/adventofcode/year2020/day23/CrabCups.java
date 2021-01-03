package com.radupetre.adventofcode.year2020.day23;

import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/23
 */
@Service
@Log4j2
public class CrabCups extends AbstractAdventSolution {

  private static final Integer STARTING_COUP_NUMBER = 1;
  private static final int CHAR_ZERO = '0';

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 23);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Integer> coupNumbers = readCoupNumbers(input);
    Map<Integer, Coup> coupsByNumber = mapCoups(coupNumbers);
    Coup firstCoup = coupsByNumber.get(coupNumbers.get(0));
    Integer maxCoup = coupNumbers.stream().mapToInt(nr -> nr).max().getAsInt();

    executeMoves(firstCoup, coupsByNumber, maxCoup, 100);
    return getCoupSequence(coupsByNumber, STARTING_COUP_NUMBER);
  }

  private List<Integer> readCoupNumbers(String input) {
    return input.trim()
        .chars()
        .mapToObj(inputChar -> inputChar - CHAR_ZERO)
        .collect(toList());
  }

  private Map<Integer, Coup> mapCoups(List<Integer> coupNumbers) {
    final ListIterator<Integer> coupNumberIterator = coupNumbers.listIterator(coupNumbers.size());
    Map<Integer, Coup> coupsByNumber = new HashMap<>();
    Coup currentCoup = null;
    Coup nextCoup = null;

    // iterate backwards to link to next coup while reading values.
    while (coupNumberIterator.hasPrevious()) {
      currentCoup = new Coup(coupNumberIterator.previous());
      coupsByNumber.put(currentCoup.number, currentCoup);
      currentCoup.nextCoup = nextCoup;
      nextCoup = currentCoup;
    }

    // link the last coup to the first
    Integer lastCoupNumber = coupNumbers.get(coupNumbers.size() - 1);
    Coup lastCoup = coupsByNumber.get(lastCoupNumber);
    lastCoup.nextCoup = currentCoup;

    return coupsByNumber;
  }

  private void executeMoves(Coup currentCoup, Map<Integer, Coup> coupsByNumber,
      Integer maxCoupNumber, int times) {
    for (int move = 0; move < times; move++) {
      // identify the 3 coups to be removed
      Coup removedStart = currentCoup.nextCoup;
      Set<Integer> removedCoupNumbers = Set.of(
          removedStart.number,
          removedStart.nextCoup.number,
          removedStart.nextCoup.nextCoup.number);

      // unlink the start
      currentCoup.nextCoup = removedStart.nextCoup.nextCoup.nextCoup;

      // find destination and insert removed coups
      final Coup destinationCoup = getDestinationCoup(currentCoup.number, maxCoupNumber,
          removedCoupNumbers, coupsByNumber);
      removedStart.nextCoup.nextCoup.nextCoup = destinationCoup.nextCoup;
      destinationCoup.nextCoup = removedStart;

      // move to the next coup
      currentCoup = currentCoup.nextCoup;
    }
  }

  private Coup getDestinationCoup(Integer currentCoupNumber, Integer maxCoupNumber,
      Set<Integer> removedCoupNumbers, Map<Integer, Coup> coupsByNumber) {

    Integer destinationCoupNumber = currentCoupNumber;
    do {
      destinationCoupNumber = destinationCoupNumber > 1
          ? destinationCoupNumber - 1
          : maxCoupNumber;
    } while (removedCoupNumbers.contains(destinationCoupNumber));

    return coupsByNumber.get(destinationCoupNumber);
  }

  private Object getCoupSequence(Map<Integer, Coup> coupsByNumber, Integer startingCoupNumber) {
    // start with first coup after the one numbered 1.
    Coup currentCoup = coupsByNumber.get(startingCoupNumber).nextCoup;
    StringBuilder coupSequence = new StringBuilder();

    while (currentCoup.number != startingCoupNumber) {
      coupSequence.append(currentCoup.number);
      currentCoup = currentCoup.nextCoup;
    }

    return coupSequence.toString();
  }

  @Override
  public Object solvePart2(String input) {
    List<Integer> coupNumbers = readCoupNumbers(input);
    Integer maxCoup = 1_000_000;
    coupNumbers = enrichWithCoupNumbersUpTo(coupNumbers, maxCoup);

    Map<Integer, Coup> coupsByNumber = mapCoups(coupNumbers);
    Coup firstCoup = coupsByNumber.get(coupNumbers.get(0));

    executeMoves(firstCoup, coupsByNumber, maxCoup, 10_000_000);
    return getCoupProduct(coupsByNumber, STARTING_COUP_NUMBER);
  }

  private List<Integer> enrichWithCoupNumbersUpTo(List<Integer> coupNumbers, Integer newMaxCoup) {
    Integer oldMaxCoup = coupNumbers.stream().mapToInt(nr -> nr).max().getAsInt();

    return Stream.concat(
        coupNumbers.stream(),
        IntStream.range(oldMaxCoup + 1, newMaxCoup + 1).mapToObj(nr -> nr)
    ).collect(toList());
  }

  private Object getCoupProduct(Map<Integer, Coup> coupsByNumber, Integer startingCoupNumber) {
    final Coup startingCoup = coupsByNumber.get(startingCoupNumber);
    Integer nextCoupNumber = startingCoup.nextCoup.number;
    Integer nextNextCoupNumber = startingCoup.nextCoup.nextCoup.number;
    return (long)nextCoupNumber * nextNextCoupNumber;
  }
}

