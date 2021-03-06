package com.radupetre.adventofcode.year2020.day02;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.util.stream.Collectors.toList;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Solving of https://adventofcode.com/2020/day/2
 */
@Service
@Log4j2
public class PasswordPhilosophy extends AbstractAdventSolution {

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 02);
  }

  @Override
  public Object solvePart1(String input) {
    final List<PasswordPolicy> passwordPolicies = parsePasswordPolicies(input);
    return countValid(passwordPolicies, this::isValidOccurrence);
  }

  @Override
  public Object solvePart2(String input) {
    final List<PasswordPolicy> passwordPolicies = parsePasswordPolicies(input);

    return countValid(passwordPolicies, this::isValidPosition);
  }

  private List<PasswordPolicy> parsePasswordPolicies(String input) {
    return getLines(input)
        .stream()
        .filter(StringUtils::hasLength)
        .map(PasswordPolicy::new)
        .collect(toList());
  }

  private long countValid(
      List<PasswordPolicy> passwordPolicies, Function<PasswordPolicy, Boolean> validator) {
    return passwordPolicies.stream()
        .filter(validator::apply)
        .count();
  }

  private boolean isValidOccurrence(PasswordPolicy passwordPolicy) {
    final long actualOccurrence = passwordPolicy.password
        .chars()
        .filter(currentChar -> currentChar == passwordPolicy.mandatoryChar)
        .count();

    return passwordPolicy.minOccurrence <= actualOccurrence
        && actualOccurrence <= passwordPolicy.maxOccurrence;
  }

  private boolean isValidPosition(PasswordPolicy passwordPolicy) {
    boolean ifFirstPositionMatch = false;
    boolean isSecondPositionMatch = false;

    if (passwordPolicy.minOccurrence <= passwordPolicy.password.length()) {
      char firstPositionChar = passwordPolicy.password.charAt(passwordPolicy.minOccurrence - 1);
      ifFirstPositionMatch = firstPositionChar == passwordPolicy.mandatoryChar;
    }

    if (passwordPolicy.maxOccurrence <= passwordPolicy.password.length()) {
      char secondPositionChar = passwordPolicy.password.charAt(passwordPolicy.maxOccurrence - 1);
      isSecondPositionMatch = secondPositionChar == passwordPolicy.mandatoryChar;
    }

    // XOR ensures the "exactly one" condition
    return ifFirstPositionMatch ^ isSecondPositionMatch;
  }
}

