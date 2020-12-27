package com.radupetre.adventofcode.year2020.day19;

import static com.radupetre.adventofcode.utils.StringUtility.getBatches;
import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/19
 */
@Service
@Log4j2
public class MonsterMessages extends AbstractAdventSolution {

  private static final String SECTION_SEPARATOR = "\n\n";
  private static final String RULE_OR_SEPARATOR = "|";
  private static final String LITERAL_VALUE_SEPARATOR = "\"";
  private static final String RULE_FRAGMENT_SEPARATOR = " ";
  private static final String RULE_ID_SEPARATOR = ":";

  private static final String REGEX_START = "(";
  private static final String REGEX_END = ")";
  private static final String REGEX_PATTERN = "^%s$";
  private static final String REGEX_REPEATED = "(%s+)";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 19);
  }

  private Map<Integer, String> ruleBodyByRuleId;
  private Map<Integer, String> ruleRegexByRuleId;
  private boolean withRecursiveRules;

  @Override
  public Object solvePart1(String input) {
    final List<String> inputSections = getBatches(input, SECTION_SEPARATOR);
    final List<String> messages = getLines(inputSections.get(1));

    ruleBodyByRuleId = parseRules(inputSections.get(0));
    ruleRegexByRuleId = new HashMap<>();
    withRecursiveRules = false;

    String ruleRegex = getRuleRegex(0);
    String regexPattern = REGEX_PATTERN.formatted(ruleRegex);

    final long messagesMatchingRules = messages.stream()
        .filter(message -> message.matches(regexPattern))
        .count();

    return messagesMatchingRules;
  }

  private String getRuleRegex(Integer ruleId) {

    if (ruleRegexByRuleId.containsKey(ruleId)) {
      // already found a regex for this rule
      return ruleRegexByRuleId.get(ruleId);
    }

    String ruleBody = ruleBodyByRuleId.get(ruleId);
    String ruleRegex;

    if (withRecursiveRules && (ruleId == 8 || ruleId == 11)) {
      // hacky way to get around recursive patterns
      ruleRegex = getRecursiveRuleRegex(ruleId);

    } else if (ruleBody.contains(LITERAL_VALUE_SEPARATOR)) {
      // this is a rule with direct value
      ruleRegex = ruleBody.replaceAll(LITERAL_VALUE_SEPARATOR, "").trim();

    } else {
      // this is a rule that depends on other rules;
      ruleRegex = getRuleRegexFromRuleBody(ruleBody);

    }

    ruleRegexByRuleId.put(ruleId, ruleRegex);
    return ruleRegex;
  }

  private String getRuleRegexFromRuleBody(String ruleBody) {
    StringBuilder ruleRegexBuilder = new StringBuilder(REGEX_START);
    final String[] ruleFragments = ruleBody.split(RULE_FRAGMENT_SEPARATOR);

    for (String ruleFragment : ruleFragments) {
      if (ruleFragment.contains(RULE_OR_SEPARATOR)) {
        // this is a OR between two rules
        ruleRegexBuilder.append(RULE_OR_SEPARATOR);
      } else {
        // this is a reference to another ruleId
        Integer referenceRuleId = Integer.parseInt(ruleFragment);
        ruleRegexBuilder
            .append(getRuleRegex(referenceRuleId));
      }
    }

    ruleRegexBuilder.append(REGEX_END);
    return ruleRegexBuilder.toString();
  }

  private Map<Integer, String> parseRules(String rulesString) {
    return getLines(rulesString).stream()
        .map(this::parseRule)
        .collect(toMap(Pair::getKey, Pair::getValue));
  }

  private Pair<Integer, String> parseRule(String ruleString) {
    final String[] ruleFragments = ruleString.split(RULE_ID_SEPARATOR);
    Integer ruleId = parseInt(ruleFragments[0].trim());
    String ruleBody = ruleFragments[1].trim();
    return Pair.of(ruleId, ruleBody);
  }

  @Override
  public Object solvePart2(String input) {
    final List<String> inputSections = getBatches(input, SECTION_SEPARATOR);
    final List<String> messages = getLines(inputSections.get(1));

    ruleBodyByRuleId = parseRules(inputSections.get(0));
    ruleRegexByRuleId = new HashMap<>();
    withRecursiveRules = true;

    String ruleRegex = getRuleRegex(0);
    String regexPattern = REGEX_PATTERN.formatted(ruleRegex);

    final long messagesMatchingRules = messages.stream()
        .filter(message -> message.matches(regexPattern))
        .count();

    return messagesMatchingRules;
  }

  private String getRecursiveRuleRegex(Integer ruleId) {
    return (ruleId == 8)
        ? getRuleRegex8(ruleId)
        : getRuleRegex11(ruleId);
  }

  private String getRuleRegex8(Integer ruleId) {
    return REGEX_REPEATED.formatted(getRuleRegex(42));
  }

  private String getRuleRegex11(Integer ruleId) {
    String ruleRegex42 = getRuleRegex(42);
    String ruleRegex31 = getRuleRegex(31);
    String ruleRegex11 = "";

    StringBuilder ruleRegexBuilder = new StringBuilder(REGEX_START);

    // 5 repetitions is specific to this input, won't work for all inputs!
    for (int repetitions = 1; repetitions < 5; repetitions++) {
      if (repetitions > 1) {
        ruleRegexBuilder.append(RULE_OR_SEPARATOR);
      }
      ruleRegexBuilder.append(REGEX_START);
      ruleRegex11 = ruleRegex42 + ruleRegex11 + ruleRegex31;
      ruleRegexBuilder.append(ruleRegex11);
      ruleRegexBuilder.append(REGEX_END);
    }

    ruleRegexBuilder.append(REGEX_END);
    return ruleRegexBuilder.toString();
  }

}

