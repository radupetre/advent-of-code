package com.radupetre.adventofcode.year2020.day14;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/14
 */
@Service
@Log4j2
public class DockingData extends AbstractAdventSolution {

  private static final String MASK_PREFIX = "mask";

  private static final int MASK_LENGTH = 35;

  private static final String WILDCARD_MASK = "X";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 14);
  }

  @Override
  public BigInteger solvePart1(String input) {
    List<String> inputLines = new ArrayList<>(getLines(input));
    return getMaskedValuesSum(inputLines);
  }

  @Override
  public Object solvePart2(String input) {
    List<String> inputLines = new ArrayList<>(getLines(input));
    return getMaskedAddressesSum(inputLines);
  }

  private BigInteger getMaskedValuesSum(List<String> inputLines) {
    Map<BigInteger, BigInteger> maskedValuesByAddress = new HashMap<>();

    BigInteger orMask = null;
    BigInteger andMask = null;

    for (String inputLine : inputLines) {
      if (inputLine.startsWith(MASK_PREFIX)) {
        String maskString = parseMask(inputLine);

        String orMaskString = maskString.replaceAll(WILDCARD_MASK, "0");
        orMask = new BigInteger(orMaskString, 2);

        String andMaskString = maskString.replaceAll(WILDCARD_MASK, "1");
        andMask = new BigInteger(andMaskString, 2);

      } else {
        MemoryWrite memWrite = parseMemoryWrite(inputLine);
        BigInteger maskedValue = memWrite.value.and(andMask).or(orMask);
        maskedValuesByAddress.put(memWrite.address, maskedValue);

      }
    }

    return sumMemoryValues(maskedValuesByAddress);
  }

  private BigInteger getMaskedAddressesSum(List<String> inputLines) {
    Map<BigInteger, BigInteger> valuesByMaskedAddress = new HashMap<>();

    BigInteger orMask = null;
    List<Integer> wildcardMaskPositions = new ArrayList<>();

    for (String inputLine : inputLines) {
      if (inputLine.startsWith(MASK_PREFIX)) {
        String maskString = parseMask(inputLine);

        String orMaskString = maskString.replaceAll(WILDCARD_MASK, "0");
        orMask = new BigInteger(orMaskString, 2);

        wildcardMaskPositions = getWildcardPositions(maskString);

      } else {
        MemoryWrite memWrite = parseMemoryWrite(inputLine);
        BigInteger maskedAddress = memWrite.address.or(orMask);
        generatePermutations(maskedAddress, wildcardMaskPositions)
            .forEach(addressPermutation ->
                valuesByMaskedAddress.put(addressPermutation, memWrite.value));
      }
    }

    return sumMemoryValues(valuesByMaskedAddress);
  }

  private Set<BigInteger> generatePermutations(BigInteger address,
      List<Integer> wildcardPositions) {
    Set<BigInteger> permutations = Set.of(address);

    for (int wildcardPosition : wildcardPositions) {
      Set<BigInteger> nextPermutation = new HashSet<>();

      for (BigInteger permutation : permutations) {
        nextPermutation.add(permutation.setBit(wildcardPosition));
        nextPermutation.add(permutation.clearBit(wildcardPosition));
      }

      permutations = nextPermutation;
    }

    return permutations;
  }

  private BigInteger sumMemoryValues(Map<BigInteger, BigInteger> valuesByAddress) {
    return valuesByAddress.values()
        .stream()
        .reduce(BigInteger::add)
        .get();
  }

  private List<Integer> getWildcardPositions(String maskString) {
    List<Integer> wildcardPositions = new ArrayList<>();
    int position = maskString.indexOf(WILDCARD_MASK);
    while (position >= 0) {
      wildcardPositions.add(MASK_LENGTH - position);
      position = maskString.indexOf(WILDCARD_MASK, position + 1);
    }

    return wildcardPositions;
  }

  private String parseMask(String inputLine) {
    return inputLine.split("=")[1].trim();
  }

  private MemoryWrite parseMemoryWrite(String inputLine) {
    String[] memoryWriteString = inputLine.split("=");

    BigInteger value = new BigInteger(memoryWriteString[1].trim());
    BigInteger location = new BigInteger(memoryWriteString[0]
        .replaceAll("\\D+", ""));

    return new MemoryWrite(location, value);
  }
}

