package com.radupetre.adventofcode.year2020.day22;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toCollection;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/22
 */
@Service
@Log4j2
public class CrabCombat extends AbstractAdventSolution {

  private static final String PLAYER_SEPARATOR = "\n\n";

  // with maximum score of 42925
  private static final Long MAX_DECK_HASH = 100000L;

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 22);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Queue<Integer>> playerDecks = parseDecks(input);
    playCombat(playerDecks.get(0), playerDecks.get(1));
    return calculateWinnerScore(playerDecks);
  }

  private void playCombat(Queue<Integer> deck1, Queue<Integer> deck2) {
    while (deck1.size() > 0 && deck2.size() > 0) {
      Integer cardFromDeck1 = deck1.remove();
      Integer cardFromDeck2 = deck2.remove();
      boolean isCard1Higher = cardFromDeck1 > cardFromDeck2;

      Queue<Integer> winningDeck = isCard1Higher ? deck1 : deck2;
      List<Integer> gainedCards = isCard1Higher
          ? List.of(cardFromDeck1, cardFromDeck2)
          : List.of(cardFromDeck2, cardFromDeck1);

      winningDeck.addAll(gainedCards);
    }
  }

  private Integer calculateWinnerScore(List<Queue<Integer>> playerDecks) {
    return playerDecks.get(0).size() > 0
        ? calculateScore(playerDecks.get(0))
        : calculateScore(playerDecks.get(1));
  }

  private Integer calculateScore(Queue<Integer> deck) {
    int totalScore = 0;
    int positionScore = deck.size();
    Iterator<Integer> deckIterator = deck.iterator();
    while (deckIterator.hasNext()) {
      totalScore += positionScore-- * deckIterator.next();
    }
    return totalScore;
  }

  private List<Queue<Integer>> parseDecks(String input) {
    return getBatches(input, PLAYER_SEPARATOR)
        .stream()
        .map(this::parseDeck)
        .collect(Collectors.toList());
  }

  private Queue<Integer> parseDeck(String deckString) {
    final List<String> deckLines = getLines(deckString);
    return IntStream.range(1, deckLines.size())
        .mapToObj(deckLines::get)
        .map(Integer::parseInt)
        .collect(toCollection(ArrayDeque::new));
  }

  @Override
  public Object solvePart2(String input) {
    final List<Queue<Integer>> playerDecks = parseDecks(input);
    playRecursiveCombat(playerDecks.get(0), playerDecks.get(1));
    return calculateWinnerScore(playerDecks);
  }

  private void playRecursiveCombat(Queue<Integer> deck1, Queue<Integer> deck2) {
    Set<Long> pastDecksPositionHashes = new HashSet<>();

    while (deck1.size() > 0 && deck2.size() > 0) {

      Long currentDecksPositionHash = calculateDecksHash(deck1, deck2);
      if (pastDecksPositionHashes.contains(currentDecksPositionHash)) {
        // this deck position was seen before, player 1 wins
        deck2.clear();
        return;
      } else {
        // note a new deck position
        pastDecksPositionHashes.add(currentDecksPositionHash);
      }

      Integer cardFromDeck1 = deck1.remove();
      Integer cardFromDeck2 = deck2.remove();

      boolean shouldPlaySubGame = deck1.size() >= cardFromDeck1 && deck2.size() >= cardFromDeck2;
      boolean isDeck1Winner;

      if (shouldPlaySubGame) {
        Queue<Integer> subGameDeck1 = extractSubGameDeck(deck1, cardFromDeck1);
        Queue<Integer> subGameDeck2 = extractSubGameDeck(deck2, cardFromDeck2);
        playRecursiveCombat(subGameDeck1, subGameDeck2);
        isDeck1Winner = subGameDeck1.size() > subGameDeck2.size();
      } else {
        isDeck1Winner = cardFromDeck1 > cardFromDeck2;
      }

      Queue<Integer> winningDeck = isDeck1Winner ? deck1 : deck2;
      List<Integer> gainedCards = isDeck1Winner
          ? List.of(cardFromDeck1, cardFromDeck2)
          : List.of(cardFromDeck2, cardFromDeck1);

      winningDeck.addAll(gainedCards);
    }
  }

  private Queue<Integer> extractSubGameDeck(Queue<Integer> existingDeck, Integer positions) {
    Iterator<Integer> existingDeckIterator = existingDeck.iterator();
    return IntStream.range(0, positions)
        .mapToObj(position -> existingDeckIterator.next())
        .collect(toCollection(ArrayDeque::new));
  }

  private Long calculateDecksHash(Queue<Integer> deck1, Queue<Integer> deck2) {
    return MAX_DECK_HASH * calculateScore(deck1) + calculateScore(deck2);
  }

}

