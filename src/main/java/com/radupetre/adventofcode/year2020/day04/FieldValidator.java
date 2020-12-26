package com.radupetre.adventofcode.year2020.day04;

import java.util.Set;

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
