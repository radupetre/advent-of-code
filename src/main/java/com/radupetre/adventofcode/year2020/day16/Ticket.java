package com.radupetre.adventofcode.year2020.day16;

import static java.util.Arrays.stream;

import java.util.List;
import java.util.stream.Collectors;

class Ticket {

  private static final String TICKET_SEPARATOR = ",";

  final List<Integer> fields;

  Ticket(String ticketString) {
    this.fields = stream(ticketString.split(TICKET_SEPARATOR))
        .map(Integer::valueOf)
        .collect(Collectors.toList());
  }
}
