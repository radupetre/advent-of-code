package com.radupetre.adventofcode.year2021.day16;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2021/day/16
 */
@Service
@Log4j2
public class PacketDecoder extends AbstractAdventSolution {

  Map<String, String> hexToBinary = Map.ofEntries(entry("0", "0000"),
      entry("1", "0001"), entry("2", "0010"), entry("3", "0011"),
      entry("4", "0100"), entry("5", "0101"), entry("6", "0110"),
      entry("7", "0111"), entry("8", "1000"), entry("9", "1001"),
      entry("A", "1010"), entry("B", "1011"), entry("C", "1100"),
      entry("D", "1101"), entry("E", "1110"), entry("F", "1111"));

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2021, 16);
  }

  @Override
  public Object solvePart1(String input) {
    Bits bits = readBits(input);
    Packet outerPacket = parsePacket(bits);
    return outerPacket.getVersionSum();
  }

  @Override
  public Object solvePart2(String input) {
    Bits bits = readBits(input);
    Packet outerPacket = parsePacket(bits);
    return outerPacket.calculateValue();
  }

  private Packet parsePacket(Bits bits) {
    Packet packet = new Packet();
    packet.version = bits.readNextInt(3);
    packet.typeId = bits.readNextInt(3);

    // this is a literal packet, parse value until last group starts with '0'
    if (packet.isLiteral()) {
      boolean isLastGroup = false;
      do {
        if (bits.peekFirstBit() == '0') {
          isLastGroup = true;
        }
        // get the next 5 bits, throw away the firs one, concatenate to value
        packet.value += bits.readNextBits(5).substring(1);
      } while (!isLastGroup);
      return packet;
    }

    // this is a operator packet, parse it and the sub-packets
    packet.lengthTypeId = bits.readNextInt(1);
    if (packet.isBitLength()) {
      // we know the length of the sub-packets
      packet.length = bits.readNextInt(15);
      Bits subPacketBits = new Bits(bits.readNextBits(packet.length));
      while (subPacketBits.containsOnes()) {
        packet.subPackets.add(parsePacket(subPacketBits));
      }
    } else {
      // we know the number of sub-packets
      packet.length = bits.readNextInt(11);
      range(0, packet.length).forEach(__ -> packet.subPackets.add(parsePacket(bits)));
    }

    return packet;
  }

  private Bits readBits(String input) {
    String bits = stream(input.split(""))
        .map(hexToBinary::get)
        .collect(joining());
    return new Bits(bits);
  }
}

@AllArgsConstructor
class Bits {

  String bits;

  public String readNextBits(int bitLength) {
    String nextBits = bits.substring(0, bitLength);
    bits = bits.substring(bitLength);
    return nextBits;
  }

  public int readNextInt(int bitLength) {
    return parseInt(readNextBits(bitLength), 2);
  }

  public char peekFirstBit() {
    return bits.charAt(0);
  }

  public boolean containsOnes() {
    return bits.contains("1");
  }
}

class Packet {

  int version;
  int typeId;
  int lengthTypeId;
  int length;
  String value = "";
  List<Packet> subPackets = new ArrayList<>();

  public boolean isLiteral() {
    return typeId == 4;
  }

  public boolean isBitLength() {
    return lengthTypeId == 0;
  }

  public int getVersionSum() {
    return version + subPackets.stream()
        .map(Packet::getVersionSum)
        .reduce(0, Integer::sum);
  }

  public long calculateValue() {
    return switch (typeId) {
      case 0 -> subPackets.stream().map(Packet::calculateValue).reduce(0L, Long::sum);
      case 1 -> subPackets.stream().map(Packet::calculateValue).reduce(1L, (a, b) -> a * b);
      case 2 -> subPackets.stream().map(Packet::calculateValue).min(Long::compareTo).get();
      case 3 -> subPackets.stream().map(Packet::calculateValue).max(Long::compareTo).get();
      case 4 -> parseLong(value, 2);
      case 5 -> subPackets.get(0).calculateValue() > subPackets.get(1).calculateValue() ? 1 : 0;
      case 6 -> subPackets.get(0).calculateValue() < subPackets.get(1).calculateValue() ? 1 : 0;
      case 7 -> subPackets.get(0).calculateValue() == subPackets.get(1).calculateValue() ? 1 : 0;
      default -> 0;
    };
  }
}
