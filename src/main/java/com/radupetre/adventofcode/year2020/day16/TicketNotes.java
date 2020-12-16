package com.radupetre.adventofcode.year2020.day16;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.utils.StringUtility;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.util.StringUtils;

class TicketNotes {

  private static final String SECTION_SEPARATOR = "\n\n";

  final List<Attribute> attributes;
  final Ticket ownTicket;
  final List<Ticket> nearbyTickets;

  TicketNotes(String ticketNotes) {
    final List<String> ticketNotesParts = StringUtility.getBatches(ticketNotes, SECTION_SEPARATOR);
    this.attributes = parseAttributes(ticketNotesParts.get(0));
    this.ownTicket = parseOwnTicket(ticketNotesParts.get(1));
    this.nearbyTickets = parseNearbyTickets(ticketNotesParts.get(2));
  }

  private List<Attribute> parseAttributes(String attributesString) {
    return getLines(attributesString)
        .stream()
        .map(Attribute::new)
        .collect(toList());
  }

  private Ticket parseOwnTicket(String ownTicketString) {
    return new Ticket(getLines(ownTicketString).get(1));
  }

  private List<Ticket> parseNearbyTickets(String nearbyTicketsString) {
    final List<String> nearbyTicketStrings = getLines(nearbyTicketsString);

    // skip the header
    return IntStream.range(1, nearbyTicketStrings.size())
        .mapToObj(nearbyTicketStrings::get)
        .filter(StringUtils::hasLength)
        .map(Ticket::new)
        .collect(toList());
  }
}
