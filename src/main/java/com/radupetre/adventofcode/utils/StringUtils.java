package com.radupetre.adventofcode.utils;

import java.util.Arrays;
import java.util.stream.Stream;

public class StringUtils {

  public static Stream<String> streamLines(String text) {
    return Arrays.stream(splitLines(text));
  }

  private static String[] splitLines(String text) {
    return text.split("\\r?\\n");
  }
}
