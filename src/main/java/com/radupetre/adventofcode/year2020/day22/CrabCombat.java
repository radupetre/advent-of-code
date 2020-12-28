package com.radupetre.adventofcode.year2020.day22;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toCollection;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
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

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 22);
  }

  @Override
  public Object solvePart1(String input) {
    final List<Queue<Integer>> playerDecks = parseDecks(input);

    playCombat(playerDecks);
    return calculateWinnerScore(playerDecks);
  }

  private void playCombat(List<Queue<Integer>> playerDecks) {
    Queue<Integer> deck1 = playerDecks.get(0);
    Queue<Integer> deck2 = playerDecks.get(1);

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
    Queue<Integer> winningDeck = playerDecks.get(0).size() > 0
        ? playerDecks.get(0)
        : playerDecks.get(1);

    int totalScore = 0;
    for (int positionScore = winningDeck.size(); positionScore > 0; positionScore--) {
      totalScore += positionScore * winningDeck.remove();
    }
    return totalScore;
  }

  private List<Queue<Integer>> parseDecks(String input) {
    return getBatches(input, PLAYER_SEPARATOR)
        .stream()
        .map(this::parsePlayerDeck)
        .collect(Collectors.toList());
  }

  @Override
  public Object solvePart2(String input) {
    return null;
  }

  private Queue<Integer> parsePlayerDeck(String deckString) {
    final List<String> deckLines = getLines(deckString);
    return IntStream.range(1, deckLines.size())
        .mapToObj(deckLines::get)
        .map(Integer::parseInt)
        .collect(toCollection(ArrayDeque::new));
  }
}

