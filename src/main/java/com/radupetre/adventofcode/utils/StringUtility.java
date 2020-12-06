package com.radupetre.adventofcode.utils;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class StringUtility {

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
}
