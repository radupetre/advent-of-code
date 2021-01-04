package com.radupetre.adventofcode.year2020.day16;

import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  @Override
  public Object solvePart1(String input) {
    notes = new TicketNotes(input);
    return calculateTicketErrorRate();
  }

  private int calculateTicketErrorRate() {
    final List<Range> consolidatedRanges = consolidateRanges(notes);
    int invalidFieldsSum = 0;

    for (Ticket ticket : notes.nearbyTickets) {
      for (Integer fieldNumber : ticket.fields) {
        if (numberNotInRanges(fieldNumber, consolidatedRanges)) {
          invalidFieldsSum += fieldNumber;
        }
      }
    }

    return invalidFieldsSum;
  }

  private List<Range> consolidateRanges(TicketNotes notes) {
    List<Range> sortedRanges = notes.attributes.stream()
        .map(attribute -> attribute.ranges)
        .flatMap(Collection::stream)
        .sorted()
        .collect(toList());

    List<Range> consolidatedRanges = new ArrayList<>();
    Range currentRange = sortedRanges.get(0);

    for (Range range : sortedRanges) {
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

  @Override
  public Object solvePart2(String input) {
    notes = new TicketNotes(input);
    List<Ticket> validTickets = getValidTickets();
    Map<String, Integer> attributeToKnownFieldsNumber = getAttributeToKnownFieldsNumber(
        validTickets);

    return multiplyValuesOnOwnTicketContaining("departure", attributeToKnownFieldsNumber);
  }

  private Map<String, Integer> getAttributeToKnownFieldsNumber(List<Ticket> validTickets) {
    Map<String, Set<Integer>> attributeToPossibleFieldNumbers = new HashMap<>();
    Map<String, Integer> attributeToKnownFieldsNumber = new HashMap<>();

    for (Attribute attribute : notes.attributes) {
      int fieldSize = notes.ownTicket.fields.size();
      for (int fieldNumber = 0; fieldNumber < fieldSize; fieldNumber++) {

        if (allFieldsInRange(fieldNumber, validTickets, attribute)) {
          final Set<Integer> possibleFieldNumbers = attributeToPossibleFieldNumbers
              .computeIfAbsent(attribute.name, k -> new HashSet<>());
          possibleFieldNumbers.add(fieldNumber);
        }
      }
    }

    // stop once all attributes are resolved
    while (attributeToKnownFieldsNumber.size() < attributeToPossibleFieldNumbers.size()) {
      for (String attribute : attributeToPossibleFieldNumbers.keySet()) {
        final Set<Integer> possibleFieldNumbers = attributeToPossibleFieldNumbers.get(attribute);

        // attribute can only match this field number
        if (possibleFieldNumbers.size() == 1) {
          int knownFieldNumber = possibleFieldNumbers.iterator().next();
          attributeToKnownFieldsNumber.put(attribute, knownFieldNumber);

          // remove the known field number from other possibilities
          attributeToPossibleFieldNumbers.values().forEach(
              possibleFieldNumbersSet -> possibleFieldNumbersSet.remove(knownFieldNumber)
          );
        }
      }
    }

    return attributeToKnownFieldsNumber;
  }

  private boolean allFieldsInRange(int fieldNumber, List<Ticket> validTickets,
      Attribute attribute) {
    for (Ticket ticket : validTickets) {
      final Integer fieldValue = ticket.fields.get(fieldNumber);
      if (numberNotInRanges(fieldValue, attribute.ranges)) {
        return false;
      }
    }
    return true;
  }

  private long multiplyValuesOnOwnTicketContaining(String attributeMatch,
      Map<String, Integer> attributeToKnownFieldsNumber) {
    return attributeToKnownFieldsNumber.keySet()
        .stream()
        .filter(attribute -> attribute.contains(attributeMatch))
        .map(attributeToKnownFieldsNumber::get)
        .mapToLong(notes.ownTicket.fields::get)
        .reduce(1, (a, b) -> a * b);
  }

  private List<Ticket> getValidTickets() {
    final List<Range> consolidatedRanges = consolidateRanges(notes);
    List<Ticket> validTickets = new ArrayList<>();

    for (Ticket ticket : notes.nearbyTickets) {
      boolean isValidTicket = true;
      for (Integer fieldNumber : ticket.fields) {
        if (numberNotInRanges(fieldNumber, consolidatedRanges)) {
          isValidTicket = false;
        }
      }
      if (isValidTicket) {
        validTickets.add(ticket);
      }
    }

    return validTickets;
  }

  private boolean numberNotInRanges(int number, List<Range> ranges) {
    for (Range range : ranges) {
      if (range.low <= number && number <= range.high) {
        return false;
      }
    }
    return true;
  }

}

