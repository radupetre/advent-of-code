package com.radupetre.adventofcode.year2020.day04;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.year2020.day04.FieldValidator.isFieldValid;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Solving of https://adventofcode.com/2020/day/4
 */
@Service
@Log4j2
public class PassportProcessing extends AbstractAdventSolution {

  private static final char KEY_VALUE_SEPARATOR = ':';

  private static final String BATCH_SEPARATOR = "\n\n";

  private static final Set<String> MANDATORY_FIELDS = Set
      .of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 4);
  }

  @Override
  public Object solvePart1(String passportBatches) {
    final List<String> batches = getBatches(passportBatches, BATCH_SEPARATOR);

    final List<Map<String, String>> passportsWithFields = batches.stream()
        .map(this::extractPassportFields)
        .collect(Collectors.toList());

    return countPassportsWithMandatoryFields(passportsWithFields);
  }

  @Override
  public Object solvePart2(String passportBatches) {
    final List<String> batches = getBatches(passportBatches, BATCH_SEPARATOR);

    final List<Map<String, String>> passportsWithFields = batches.stream()
        .map(this::extractPassportFields)
        .collect(Collectors.toList());

    return countPassportsWithValidData(passportsWithFields);
  }

  private long countPassportsWithMandatoryFields(List<Map<String, String>> passportsWithFields) {
    return passportsWithFields
        .stream()
        .filter(this::hasMandatoryFields)
        .count();
  }

  private long countPassportsWithValidData(List<Map<String, String>> passportsWithFields) {
    return passportsWithFields
        .stream()
        .filter(this::hasMandatoryFields)
        .filter(this::hasValidFields)
        .count();
  }

  private boolean hasMandatoryFields(Map<String, String> passportsWithFields) {
    return passportsWithFields
        .keySet()
        .containsAll(MANDATORY_FIELDS);
  }

  private boolean hasValidFields(Map<String, String> passportsWithFields) {
    boolean isValid = true;
    for (Entry<String, String> passportField : passportsWithFields.entrySet()) {
      if (!isValid) {
        return false;
      }

      isValid = isFieldValid(passportField.getKey(), passportField.getValue());
    }
    return isValid;
  }

  public Map<String, String> extractPassportFields(String passportBatchInformation) {
    // replace non-printable
    passportBatchInformation = passportBatchInformation.replaceAll("\\p{C}", " ");

    return stream(passportBatchInformation.split(" "))
        .map(String::trim)
        .filter(StringUtils::hasText)
        .collect(toMap(this::extractKey, this::extractValue));
  }

  private String extractKey(String keyValuePair) {
    return keyValuePair.substring(0, keyValuePair.indexOf(KEY_VALUE_SEPARATOR));
  }

  private String extractValue(String keyValuePair) {
    return keyValuePair.substring(keyValuePair.indexOf(KEY_VALUE_SEPARATOR) + 1);
  }
}

class FieldValidator {

  private static final Set<String> VALID_EYE_COLOURS = Set
      .of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

  static boolean isFieldValid(String key, String value) {
    switch (key) {
      case "byr":
        return hasOnlyDigits(value) && hasLimits(value, "1920", "2002");
      case "iyr":
        return hasOnlyDigits(value) && hasLimits(value, "2010", "2020");
      case "eyr":
        return hasOnlyDigits(value) && hasLimits(value, "2020", "2030");
      case "hgt":
        return hasValidHeight(value);
      case "hcl":
        return hasValidHairColour(value);
      case "ecl":
        return VALID_EYE_COLOURS.contains(value);
      case "pid":
        return hasOnlyDigits(value) && value.length() == 9;
      default:
        return true;
    }
  }

  private static boolean hasOnlyDigits(String str) {
    return str.matches("[0-9]+");
  }

  private static boolean hasOnlyLettersOrDigits(String str) {
    return str.matches("[0-9A-Za-z]+");
  }

  private static boolean hasLimits(String str, String lower, String higher) {
    return str.compareTo(lower) >= 0 && str.compareTo(higher) <= 0;
  }

  private static boolean hasValidHairColour(String colourCode) {
    return colourCode.length() == 7
        && '#' == colourCode.charAt(0)
        && hasOnlyLettersOrDigits(colourCode.substring(1));
  }

  private static boolean hasValidHeight(String height) {
    String unit = height.substring(height.length() - 2);
    String value = height.substring(0, height.length() - 2);

    switch (unit) {
      case "cm":
        return hasOnlyDigits(value) && hasLimits(value, "150", "193");
      case "in":
        return hasOnlyDigits(value) && hasLimits(value, "59", "76");
      default:
        return false;
    }
  }
}
