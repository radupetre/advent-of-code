package com.radupetre.adventofcode.year2022.day13;

import static com.radupetre.adventofcode.utils.StringUtility.EMPTY_LINE;
import static com.radupetre.adventofcode.utils.StringUtility.getBatchesAsStream;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static com.radupetre.adventofcode.year2022.day13.DistressSignal.Packet.newListPacket;
import static com.radupetre.adventofcode.year2022.day13.DistressSignal.Packet.newNumberPacket;
import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import com.radupetre.adventofcode.utils.StringUtility;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2022/day/13
 */
@Service
@Log4j2
public class DistressSignal extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2022, 13);
  }

  private final PacketComparator comparator = new PacketComparator();

  @Override
  public Object solvePart1(String input) {
    var pairs = getBatchesAsStream(input, EMPTY_LINE)
        .map(this::parsePacketPair)
        .collect(toList());

    return IntStream.range(0, pairs.size())
        .filter(i -> comparator.compare(pairs.get(i).getLeft(), pairs.get(i).getRight()) < 0)
        .map(i -> i + 1)
        .sum();
  }

  @Override
  public Object solvePart2(String input) {
    var inputPackets = getBatchesAsStream(input, EMPTY_LINE)
        .flatMap(StringUtility::getLinesAsStream)
        .map(this::parsePacket);

    var addedPacket1 = parsePacket("[[2]]");
    var addedPacket2 = parsePacket("[[6]]");

    var orderedPackets = Stream.concat(inputPackets, Stream.of(addedPacket1, addedPacket2))
        .sorted(comparator)
        .collect(toList());

    return (orderedPackets.indexOf(addedPacket1) + 1) * (orderedPackets.indexOf(addedPacket2) + 1);
  }

  private Pair<Packet, Packet> parsePacketPair(String input) {
    var pairs = getLines(input).stream().map(this::parsePacket).collect(toList());
    return Pair.of(pairs.get(0), pairs.get(1));
  }

  private Packet parsePacket(String input) {
    Packet root = null;
    Stack<Packet> packets = new Stack<>();
    var chars = input.chars()
        .mapToObj(c -> (char) c)
        .collect(toCollection(LinkedList::new));

    while (!chars.isEmpty()) {
      if (chars.peek() == '[') {
        Packet packet = newListPacket();
        if (packets.isEmpty()) {
          root = packet;
        } else {
          packets.peek().items.add(packet);
        }
        packets.push(packet);
        chars.pop();
      } else if (chars.peek() == ']') {
        packets.pop();
        chars.pop();
      } else if (chars.peek() == ',') {
        chars.pop();
      } else {
        StringBuilder number = new StringBuilder();
        while (isDigit(chars.peek())) {
          number.append(chars.pop());
        }
        packets.peek().items.add(newNumberPacket(parseInt(number.toString())));
      }
    }
    return root;
  }

  @RequiredArgsConstructor
  static class Packet {

    final boolean isList;
    final Integer number;
    final List<Packet> items = new ArrayList<>();

    static Packet newNumberPacket(int number) {
      return new Packet(false, number);
    }

    static Packet newListPacket() {
      return new Packet(true, null);
    }

    static Packet newListPacket(Packet innerPacket) {
      var packet = new Packet(true, null);
      packet.items.add(innerPacket);
      return packet;
    }
  }

  static class PacketComparator implements Comparator<Packet> {

    @Override
    public int compare(Packet o1, Packet o2) {
      if (!o1.isList && !o2.isList) {
        return Integer.compare(o1.number, o2.number);
      } else {
        o1 = o1.isList ? o1 : newListPacket(o1);
        o2 = o2.isList ? o2 : newListPacket(o2);

        for (int index = 0; index < max(o1.items.size(), o2.items.size()); index++) {
          if (index >= o1.items.size()) {
            return -1;
          }
          if (index >= o2.items.size()) {
            return 1;
          }
          var subCompare = this.compare(o1.items.get(index), o2.items.get(index));
          if (subCompare != 0) {
            return subCompare;
          }
        }
      }
      return 0;
    }
  }

}