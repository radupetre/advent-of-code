package com.radupetre.adventofcode.year2020.day16;

import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.Result;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/16
 */
@Service
@Log4j2
public class TicketTranslation extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 16);
  }

  TicketNotes notes;
  List<Ticket> validTickets = new ArrayList<>();

  @Override
  public Result solve(String input) {
    notes = new TicketNotes(input);

    int ticketErrorRate = calculateTicketErrorRate();
    log.info("Ticket scanning error rate: %s".formatted(ticketErrorRate));

    int ownTicketMultipliedValues = multiplyValuesOnOwnTicket();
    log.info("Multiplied values on own ticket: %s".formatted(ownTicketMultipliedValues));

    return new Result(ticketErrorRate, ownTicketMultipliedValues);
  }

  private int calculateTicketErrorRate() {
    final List<Range> consolidatedRanges = consolidateRanges(notes);
    int invalidFieldsSum = 0;

    for (Ticket ticket : notes.nearbyTickets) {
      boolean isValidTicket = true;
      for (Integer fieldNumber : ticket.fields) {
        if (numberNotInRanges(fieldNumber, consolidatedRanges)) {
          isValidTicket = false;
          invalidFieldsSum += fieldNumber;
        }
      }

      if (isValidTicket) {
        validTickets.add(ticket);
      }
    }

    return invalidFieldsSum;
  }

  private int multiplyValuesOnOwnTicket() {
    // TODO

    return 0;
  }

  private boolean numberNotInRanges(int number, List<Range> ranges) {
    for (Range range : ranges) {
      if (range.low <= number && number <= range.high) {
        return false;
      }
    }
    return true;
  }

  private List<Range> consolidateRanges(TicketNotes notes) {
    List<Range> sortedRanges = notes.attributes.stream()
        .map(attribute -> attribute.ranges)
        .flatMap(Collection::stream)
        .sorted()
        .collect(toList());

    List<Range> consolidatedRanges = new ArrayList<>();
    Range currentRange = sortedRanges.get(0);

    for (Range range : sortedRanges){
      if (currentRange.hasOverlap(range)) {
        currentRange = currentRange.merge(range);
      } else {
        consolidatedRanges.add(currentRange);
        currentRange = range;
      }
    }
    consolidatedRanges.add(currentRange);

    return consolidatedRanges;
  }
}

