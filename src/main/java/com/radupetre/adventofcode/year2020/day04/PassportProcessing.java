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
    List<Map<String, String>> passportsWithFields = parsePassportBatches(passportBatches);
    return countPassportsWithMandatoryFields(passportsWithFields);
  }

  @Override
  public Object solvePart2(String passportBatches) {
    List<Map<String, String>> passportsWithFields = parsePassportBatches(passportBatches);
    return countPassportsWithValidData(passportsWithFields);
  }

  private List<Map<String, String>> parsePassportBatches(String passportBatches) {
    final List<String> batches = getBatches(passportBatches, BATCH_SEPARATOR);

    return batches.stream()
        .map(this::extractPassportFields)
        .collect(Collectors.toList());
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

