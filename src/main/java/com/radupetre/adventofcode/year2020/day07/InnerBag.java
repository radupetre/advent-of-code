package com.radupetre.adventofcode.year2020.day07;

import static java.lang.Integer.parseInt;

import lombok.Getter;

@Getter
class InnerBag {

  private final String name;
  private final int quantity;

  InnerBag(String innerBagString) {
    this.quantity = parseInt(innerBagString.split(" ")[0]);
    this.name = innerBagString
        .replaceAll("\\d", "")
        .replaceAll("bag.*", "")
        .trim();
  }
}
