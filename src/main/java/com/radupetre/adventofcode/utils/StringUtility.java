package com.radupetre.adventofcode.utils;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class StringUtility {

  public static final String EMPTY_LINE = "\n\n";
  public static final String NEW_LINE = "\n";
  public static final String COMMA = ",";
  public static final String SPACE = " ";


  public static List<String> getLines(String text) {
    return stream(splitLines(text))
        .collect(toList());
  }

  public static List<String> getBatches(String text, String separator) {
    return stream(text.split(separator))
        .collect(toList());
  }

  private static String[] splitLines(String text) {
    return text.split("\\r?\\n");
  }

  public static String cleanString(String toClean) {
    return toClean.replaceAll("\\p{C}", "");
  }

  public static String cleanSpaces(String toClean) {
    return toClean.replaceAll("\\s{2,}", " ").trim();
  }
}
