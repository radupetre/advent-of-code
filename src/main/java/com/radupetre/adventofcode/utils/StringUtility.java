package com.radupetre.adventofcode.utils;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

public class StringUtility {

  public static final String EMPTY_LINE = "\\r?\\n\\r?\\n";
  public static final String NEW_LINE = "\n";
  public static final String PIPE = "\\|";
  public static final String COMMA = ",";
  public static final String COLON = ":";
  public static final String SPACE = " ";
  public static final String EQUALS = "=";
  public static final String ARROW = "->";


  public static List<String> getLines(String text) {
    return stream(splitLines(text))
        .collect(toList());
  }

  public static Stream<String> getLinesAsStream(String text) {
    return stream(splitLines(text));
  }

  public static List<String> getBatches(String text, String separator) {
    return stream(text.split(separator))
        .collect(toList());
  }

  public static Stream<String> getBatchesAsStream(String text, String separator) {
    return stream(text.split(separator));
  }

  private static String[] splitLines(String text) {
    return text.split("\\r?\\n");
  }

  public static String cleanString(String toClean) {
    return toClean.replaceAll("\\p{C}", "");
  }

  public static String cleanCarriageReturn(String toClean) {
    return toClean.replaceAll("\r", "");
  }

  public static String cleanSpaces(String toClean) {
    return toClean.replaceAll("\\s{2,}", " ").trim();
  }

  public static String keepDigits(String toClean) {
    return toClean.replaceAll("[^\\d-]", " ");
  }
}
