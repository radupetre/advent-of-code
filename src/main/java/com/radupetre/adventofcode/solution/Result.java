package com.radupetre.adventofcode.solution;

import lombok.Getter;

@Getter
public class Result {
  private final String first;
  private final String second;

  public Result(Object first, Object second) {
    this.first = first.toString();
    this.second = second.toString();
  }

  @Override
  public String toString() {
    return "%s, %s".formatted(first, second);
  }
}
